package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(
            """
                    SELECT p
                        FROM Project p
                            WHERE
                            p.id IN (
                            SELECT pm.projectMemberId.projectId
                              FROM ProjectMember pm
                                WHERE
                                    pm.projectMemberId.memberId = :userId
                                    AND pm.projectMemberId.projectId=:projectId
                            )
                            and p.deletedAt IS NULL
                    """
    )
    Optional<Project> findProjectByProjectIdAndUserId(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId);


    @Query(
            """
            SELECT p
                FROM Project p
                    WHERE
                        p.id IN (
                                    SELECT pm.projectMemberId.projectId
                                      FROM ProjectMember pm
                                        WHERE 
                                            pm.user.id = :userId
                                    )
                        AND p.deletedAt IS NULL 
                        ORDER BY p.updatedAt desc
            """
    )
    List<Project> getAllProjectsByUserId(Long userId);

}
