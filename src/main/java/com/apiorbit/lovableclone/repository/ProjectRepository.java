package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
