package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.mapper.ProjectMapper;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;

    @Override
    public List<ProjectSummeryResponse> getAllProjects(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        List<Project> listOfProjects = projectRepository.findByUser(user);

        return projectMapper.toProjectSummeryResponse(listOfProjects);
    }

    @Override
    public ProjectResponse getUserProjectById(
            Long projectId,
            Long userId) {
        return null;
    }

    @Override
    @Transactional
    public ProjectResponse createProject(
            Long userId,
            ProjectRequest projectRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Project project = Project.builder()
                .user(user)
                .name(projectRequest.title())
                .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectRequest updateProject(
            Long id,
            Long userId,
            ProjectRequest project) {
        return null;
    }

    @Override
    public void softDelete(Long id) {

    }
}
