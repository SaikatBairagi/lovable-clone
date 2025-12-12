package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);

    @Query(
            """
                    SELECT p
                                FROM Project p
                                JOIN FETCH p.user
                                            WHERE
                                            p.id = :projectId
                                            and p.deletedAt IS NULL
                                            and p.user.id = :userId
                    """
    )
    Optional<Project> findProjectByProjectIdAndUserId(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId);

}
