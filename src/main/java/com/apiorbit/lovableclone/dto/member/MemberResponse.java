package com.apiorbit.lovableclone.dto.member;

import com.apiorbit.lovableclone.enumaration.MemberRole;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String avatar,
        MemberRole role
) {
}
