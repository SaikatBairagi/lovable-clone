package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.member.InviteMemberRequest;
import com.apiorbit.lovableclone.dto.member.MemberResponse;
import com.apiorbit.lovableclone.dto.member.UpdateMemberRole;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectMember;
import com.apiorbit.lovableclone.entity.ProjectMemberId;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.ProjectInviteRequest;
import com.apiorbit.lovableclone.error.BadRequestException;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.mapper.MemberResponseMapper;
import com.apiorbit.lovableclone.repository.ProjectMemberRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    MemberResponseMapper memberResponseMapper;
    UserRepository userRepository;
    AuthUtil authUtil;

    @Override
    public List<MemberResponse> getAllMembers(
            Long projectId) {
        log.info("In the MemberResponse method");
        Long userId = authUtil.getUserId();

        List<MemberResponse> memberList = new ArrayList<>();
        //Adding the Owner in the list
       // memberList.add(memberResponseMapper.toMemberResponse(project.getUser()));

        List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectId(projectId);
        log.info("projectMemberList: " + projectMemberList);
        for(ProjectMember projectMember: projectMemberList){
            User user = projectMember.getUser();
            memberList.add(memberResponseMapper.toMemberResponse(user, projectMember));
        }

        return memberList;
    }

    @Override
    public void deleteMember(
            Long projectId,
            Long memberId) {
        log.info("In the deleteMember method");
        Long userId = authUtil.getUserId();
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow(()-> new NoResourceFoundException("ProjectMember", projectMemberId.toString()));
        projectMemberRepository.delete(projectMember);
    }

    @Override
    public MemberResponse updateMemberRole(
            Long projectId,
            Long memberId,
            UpdateMemberRole memberRole) {
        log.info("In the updateMemberRole method");
        Long userId = authUtil.getUserId();
        if(userId.equals(memberId))
            throw new RuntimeException("Could not change owner role");
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow(()-> new NoResourceFoundException("ProjectMember", projectMemberId.toString()));
        projectMember.setMemberRole(memberRole.role());
        projectMember = projectMemberRepository.saveAndFlush(projectMember);
        return memberResponseMapper.toMemberResponse(projectMember.getUser(), projectMember);
    }

    @Override
    @Transactional
    public MemberResponse inviteMember(
            Long projectId,
            InviteMemberRequest memberRequest) {
        Long userId = authUtil.getUserId();
        Project project = projectRepository.findProjectByProjectIdAndUserId(projectId, userId).orElseThrow(()-> new NoResourceFoundException("Project", projectId.toString()));
        // Fetch the Invitee's user profile
        User invitee = userRepository.findByEmail(memberRequest.email()).orElseThrow(()-> new NoResourceFoundException("User", memberRequest.email()));
        //checking if the user is already been added to the project
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());
        log.info("projectMemberId: " + projectMemberId);
        Optional<ProjectMember> projectMemberOptional = projectMemberRepository.findById(projectMemberId);
        log.info("projectMemberOptional: " + projectMemberOptional);
        if(projectMemberOptional.isPresent()){
            throw new BadRequestException("User already part of this project");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .projectMemberId(projectMemberId)
                .project(project)
                .user(invitee)
                .memberRole(memberRequest.role())
                .inviteRequest(ProjectInviteRequest.PENDING)
                .build();
        log.info("projectMember: " + projectMember);
        projectMember = projectMemberRepository.saveAndFlush(projectMember);

        return memberResponseMapper.toMemberResponse(projectMember.getUser(), projectMember);
    }
}
