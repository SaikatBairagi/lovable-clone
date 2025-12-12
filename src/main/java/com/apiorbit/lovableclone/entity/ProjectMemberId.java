package com.apiorbit.lovableclone.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMemberId {

    Long projectId;
    Long memberId;
}
