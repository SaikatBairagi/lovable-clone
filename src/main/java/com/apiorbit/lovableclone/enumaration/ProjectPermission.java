package com.apiorbit.lovableclone.enumaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermission {

    VIEW("project:view"),
    DELETE("project:delete"),
    EDIT("project:edit"),
    MANAGE_MEMBERS("project:manage_members");

    private final String value;
}
