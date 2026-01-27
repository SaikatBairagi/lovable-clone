package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.project.FileContentResponse;
import com.apiorbit.lovableclone.dto.project.FileNode;
import com.apiorbit.lovableclone.service.FileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("NullableProblems")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {

    FileService fileService;

    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId) {
        Long userId = 1L;
        return ResponseEntity.ok().body(fileService.getFileTree(projectId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @PathVariable String path) {
        Long userId = 1L;
        return ResponseEntity.ok().body(fileService.getFileContent(projectId, path));
    }

}
