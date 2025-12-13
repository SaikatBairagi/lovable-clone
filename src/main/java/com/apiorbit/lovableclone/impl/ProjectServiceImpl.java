package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectMember;
import com.apiorbit.lovableclone.entity.ProjectMemberId;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.MemberRole;
import com.apiorbit.lovableclone.mapper.ProjectMapper;
import com.apiorbit.lovableclone.repository.ProjectMemberRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;

    @Override
    public List<ProjectSummeryResponse> getAllProjects(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        List<Project> listOfProjects = projectRepository.findByUser(user);
        listOfProjects = listOfProjects
                .stream()
                .filter(project -> project.getDeletedAt()== null)
                .collect(Collectors.toList()); // get all projects that are not deleted
        return projectMapper.toProjectSummeryResponse(listOfProjects);
    }

    @Override
    public ProjectResponse getUserProjectById(
            Long projectId,
            Long userId) {
        Project project = projectRepository.findProjectByProjectIdAndUserId(projectId, userId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        return projectMapper.toProjectResponse(project);
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
        project = projectRepository.saveAndFlush(project);
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), user.getId());
        ProjectMember projectMember = ProjectMember
                .builder()
                .projectMemberId(projectMemberId)
                .user(user)
                .project(project)
                .memberRole(MemberRole.ADMIN)
                .build();
        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(
            Long id,
            Long userId,
            ProjectRequest projectRequest) {
        Project project = projectRepository.findProjectByProjectIdAndUserId(id, userId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not owner of this project");
        }
        project.setName(projectRequest.title());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @Transactional
    public void softDelete(
            Long id,
            Long userId) {
        Project project = projectRepository.findProjectByProjectIdAndUserId(id, userId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);

    }
}
