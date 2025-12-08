package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.member.InviteMemberRequest;
import com.apiorbit.lovableclone.dto.member.MemberResponse;
import com.apiorbit.lovableclone.dto.member.UpdateMemberRole;
import com.apiorbit.lovableclone.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<MemberResponse> getAllMembers(
            Long userId,
            Long projectId) {
        return List.of();
    }

    @Override
    public Void deleteMember(
            Long projectId,
            Long userId,
            Long memberId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(
            Long projectId,
            Long userId,
            Long memberId,
            UpdateMemberRole memberRole) {
        return null;
    }

    @Override
    public MemberResponse inviteMember(
            Long projectId,
            Long userId,
            InviteMemberRequest memberRequest) {
        return null;
    }
}
