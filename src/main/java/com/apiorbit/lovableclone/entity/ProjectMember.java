package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.MemberRole;
import com.apiorbit.lovableclone.enumaration.ProjectInviteRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_members")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {

    @EmbeddedId
    ProjectMemberId projectMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    Project project;

    @Enumerated(EnumType.STRING)
    MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    ProjectInviteRequest inviteRequest;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant invitedAt;

    Instant acceptedAt;


}
