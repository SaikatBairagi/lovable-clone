package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.project.FileContentResponse;
import com.apiorbit.lovableclone.dto.project.FileNode;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(
            Long userId,
            Long projectId);

    FileContentResponse getFileContent(
            Long projectId,
            Long userId,
            String path);
}
