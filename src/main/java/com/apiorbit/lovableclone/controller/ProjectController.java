package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.service.ProjectService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("NullableProblems")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/project")
public class ProjectController {

    ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<ProjectSummeryResponse>> getAllProjectsOfUser() {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectService.getAllProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectService.getUserProjectById(id, userId));

    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest project) {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(userId, project));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectRequest> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest project) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectService.updateProject(id, userId, project));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        Long userId = 1L;
        projectService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
