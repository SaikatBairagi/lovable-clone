package com.apiorbit.lovableclone.security;

import com.apiorbit.lovableclone.entity.ProjectMemberId;
import com.apiorbit.lovableclone.enumaration.ProjectPermission;
import com.apiorbit.lovableclone.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepository;


    public boolean canAccessProject(Long projectId) {
        Long userId = authUtil.getUserId();
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, userId);
        return projectMemberRepository.findMemberRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(ProjectPermission.VIEW))
                .orElseThrow(()-> new AccessDeniedException("User doesn't have permission to view project "+projectId));
    }

    public boolean canEditProject(Long projectId) {
        Long userId = authUtil.getUserId();
        return projectMemberRepository.findMemberRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(ProjectPermission.EDIT))
                .orElseThrow(() -> new AccessDeniedException("User doesn't have permission to edit project "+projectId));
    }

    public boolean canDeleteProject(Long projectId) {
        Long userId = authUtil.getUserId();
        return projectMemberRepository.findMemberRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(ProjectPermission.DELETE))
                .orElseThrow(() -> new AccessDeniedException("User doesn't have permission to delete project "+projectId));
    }

    public boolean canAddMember(Long projectId) {
        Long userId = authUtil.getUserId();
        return projectMemberRepository.findMemberRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(ProjectPermission.MANAGE_MEMBERS))
                .orElseThrow(() -> new AccessDeniedException("User doesn't have permission to manage members "+projectId));
    }
}
