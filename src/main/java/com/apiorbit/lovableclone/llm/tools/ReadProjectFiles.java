package com.apiorbit.lovableclone.llm.tools;

import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ReadProjectFiles {

    private final FileService  fileService;
    private final Long projectId;


    @Tool(name = "read_files",
            description = "Read the content of files. Only input the file names present inside the FILE_TREE. DO NOT input any path which is not present under the FILE_TREE.")
    public List<String> readProjectFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/App.tsx'])")
            List<String> paths) {
        List<String> files = new ArrayList<>();
        for (String path : paths) {
            String cleanPath = (path.startsWith("/"))?path.substring(1): path;
            log.info("Reading project files from: {}", cleanPath);
            String fileContent = fileService.getFileContent(projectId, cleanPath).content();
            files.add(String.format(
                    "--- START OF FILE: %s---\n%s\n---END OF FILE---",
                    cleanPath,
                    fileContent
            ));
        }
        return files;
    }
}
