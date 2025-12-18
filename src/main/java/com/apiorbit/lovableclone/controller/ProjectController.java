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
        return ResponseEntity.ok().body(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok().body(projectService.getUserProjectById(id));

    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(project));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest project) {
        return ResponseEntity.ok().body(projectService.updateProject(id, project));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        projectService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
