package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectMember;
import com.apiorbit.lovableclone.entity.ProjectMemberId;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.MemberRole;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.mapper.ProjectMapper;
import com.apiorbit.lovableclone.repository.ProjectMemberRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;

    @Override
    public List<ProjectSummeryResponse> getAllProjects() {
        Long userId = authUtil.getUserId();
        log.info("Getting all projects by user id {}", userId);
        List<Project> allProjectsByUser = projectRepository.getAllProjectsByUserId(userId);
        log.debug("List of Projects {}", allProjectsByUser);
        return projectMapper.toProjectSummeryResponse(allProjectsByUser);

    }

    @Override
    public ProjectResponse getUserProjectById(
            Long projectId) {
        Long userId = authUtil.getUserId();
        Project project = projectRepository.findProjectByProjectIdAndUserId(projectId, userId).orElseThrow(() -> new NoResourceFoundException("Project", projectId.toString()));
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, userId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow(() -> new NoResourceFoundException("ProjectMember", projectId.toString()));
        return projectMapper.toProjectResponse(project, projectMember);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        Long userId = authUtil.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Project project = Project.builder()
                .name(projectRequest.title())
                .build();
        project = projectRepository.saveAndFlush(project);
        log.info("Project created: {}", project);
        log.info("User created: {}", user);
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), user.getId());
        log.info("ProjectMember created: {}", projectMemberId);
        ProjectMember projectMember = ProjectMember
                .builder()
                .projectMemberId(projectMemberId)
                .project(project)
                .user(user)
                .memberRole(MemberRole.ADMIN)
                .build();
        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(
            Long id,
            ProjectRequest projectRequest) {
        Long userId = authUtil.getUserId();
        Project project = projectRepository.findProjectByProjectIdAndUserId(id, userId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        project.setName(projectRequest.title());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @Transactional
    public void softDelete(
            Long id) {
        Long userId = authUtil.getUserId();
        Project project = projectRepository.findProjectByProjectIdAndUserId(id, userId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);

    }
}
