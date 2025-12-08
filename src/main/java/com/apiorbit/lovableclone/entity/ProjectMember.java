package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.MemberRole;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {

    ProjectMemberId projectMemberId;
    MemberRole memberRole;
    User invitedBy;
    Project project;
    Instant invitedAt;
    Instant acceptedAt;


}
