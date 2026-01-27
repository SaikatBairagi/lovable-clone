package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.project.FileContentResponse;
import com.apiorbit.lovableclone.dto.project.FileNode;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(
            Long projectId);

    FileContentResponse getFileContent(
            Long projectId,
            String path);

    void saveFile(
            Long projectId,
            String filePath,
            String fileContent);
}
