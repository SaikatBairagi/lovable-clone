package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.config.BucketConfig;
import com.apiorbit.lovableclone.dto.project.FileContentResponse;
import com.apiorbit.lovableclone.dto.project.FileNode;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectFile;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.mapper.ProjectFileMapper;
import com.apiorbit.lovableclone.repository.ProjectFileRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProjectRepository projectRepository;
    private final MinioClient minioClient;
    private final ProjectFileRepository projectFileRepository;
    private final ProjectFileMapper projectFileMapper;

    @Value("${minio.bucket}")
    private String bucket;



    @Override
    public List<FileNode> getFileTree(
            Long projectId) {
        List<ProjectFile> getFileTreeOfProject = projectFileRepository.findByProjectId(projectId);
        return projectFileMapper.toFileNode(getFileTreeOfProject);
    }

    @Override
    public FileContentResponse getFileContent(
            Long projectId,
            String path) {
        String objectName = projectId + "/" + path;
        try (
                InputStream is = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .build())) {

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new FileContentResponse(path, content);
        } catch (Exception e) {
            log.error("Failed to read file: {}/{}", projectId, path, e);
            throw new RuntimeException("Failed to read file content in FileContentResponse", e);
        }
    }

    @Override
    public void saveFile(
            Long projectId,
            String path,
            String content) {
        log.info("Project Id: {}", projectId);
        log.info("Saving file path {} & content {}", path, content);


        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new NoResourceFoundException("Project", projectId.toString())
        );

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String objectKey = projectId + "/" + cleanPath;

        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            // saving the file content
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(inputStream, contentBytes.length, -1)
                            .contentType(determineContentType(path))
                            .build());

            // Saving the metaData
            ProjectFile file = projectFileRepository.findByProjectIdAndPath(projectId, cleanPath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .minioObjectKey(objectKey) // Use the key we generated
                            .createdAt(Instant.now())
                            .build());

            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);
            log.info("Saved file: {}", objectKey);
        } catch (Exception e) {
            log.error("Failed to save file {}/{}", projectId, cleanPath, e);
            throw new RuntimeException("File save failed", e);
        }

    }


    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) return type;
        if (path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")) return "text/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".css")) return "text/css";

        return "text/plain";
    }
}
