package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
}
