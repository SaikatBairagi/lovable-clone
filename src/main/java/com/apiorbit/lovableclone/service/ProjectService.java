package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
    List<ProjectSummeryResponse> getAllProjects(Long userId);

    ProjectResponse getUserProjectById(
            Long projectId,
            Long userId);

    ProjectResponse createProject(
            Long userId,
            ProjectRequest project);

    ProjectRequest updateProject(
            Long id,
            Long userId,
            ProjectRequest project);

    void softDelete(Long id);
}
