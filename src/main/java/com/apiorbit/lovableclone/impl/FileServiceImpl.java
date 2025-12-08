package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.project.FileContentResponse;
import com.apiorbit.lovableclone.dto.project.FileNode;
import com.apiorbit.lovableclone.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getFileTree(
            Long userId,
            Long projectId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(
            Long projectId,
            Long userId,
            String path) {
        return null;
    }
}
