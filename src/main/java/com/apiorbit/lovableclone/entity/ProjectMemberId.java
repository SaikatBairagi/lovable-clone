package com.apiorbit.lovableclone.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMemberId {

    Long projectId;
    Long memberId;
}
