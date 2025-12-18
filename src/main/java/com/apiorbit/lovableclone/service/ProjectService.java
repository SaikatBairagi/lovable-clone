package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
    List<ProjectSummeryResponse> getAllProjects();

    ProjectResponse getUserProjectById(
            Long projectId);

    ProjectResponse createProject(ProjectRequest project);

    ProjectResponse updateProject(
            Long id,
            ProjectRequest project);

    void softDelete(
            Long id);
}
