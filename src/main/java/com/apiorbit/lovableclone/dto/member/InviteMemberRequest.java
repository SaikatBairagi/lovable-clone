package com.apiorbit.lovableclone.dto.member;

import com.apiorbit.lovableclone.enumaration.MemberRole;

public record InviteMemberRequest(
        String email,
        MemberRole role
) {
}
