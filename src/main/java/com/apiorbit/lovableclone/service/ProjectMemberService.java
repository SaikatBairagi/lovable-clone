package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.member.InviteMemberRequest;
import com.apiorbit.lovableclone.dto.member.MemberResponse;
import com.apiorbit.lovableclone.dto.member.UpdateMemberRole;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getAllMembers(
            Long projectId);

    void deleteMember(
            Long projectId,
            Long memberId);

    MemberResponse updateMemberRole(
            Long projectId,
            Long memberId,
            UpdateMemberRole memberRole);

    MemberResponse inviteMember(
            Long projectId,
            InviteMemberRequest memberRequest);
}
