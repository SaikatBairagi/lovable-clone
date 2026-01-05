package com.apiorbit.lovableclone.enumaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.apiorbit.lovableclone.enumaration.ProjectPermission.*;

@RequiredArgsConstructor
@Getter
public enum MemberRole {
    EDITOR(Set.of(VIEW, EDIT)),
    ADMIN(Set.of(VIEW, EDIT, DELETE, MANAGE_MEMBERS)),
    VIEWER(Set.of(VIEW));

    private final Set permissions;
}
