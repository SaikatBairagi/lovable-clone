package com.apiorbit.lovableclone.dto.member;

import com.apiorbit.lovableclone.enumaration.MemberRole;
import org.mapstruct.Mapping;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String avatar,
        MemberRole role
) {
}
