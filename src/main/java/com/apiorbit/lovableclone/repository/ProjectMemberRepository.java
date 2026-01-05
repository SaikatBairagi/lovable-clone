package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.ProjectMember;
import com.apiorbit.lovableclone.entity.ProjectMemberId;
import com.apiorbit.lovableclone.enumaration.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByProjectId(Long projectId);

    @Query(
            """
            SELECT pm.memberRole
            FROM ProjectMember pm
            WHERE
                        pm.project.id=:projectId
                                    AND pm.user.id=:userId
            """
    )
    Optional<MemberRole> findMemberRoleByProjectIdAndUserId(Long projectId, Long userId);
}
