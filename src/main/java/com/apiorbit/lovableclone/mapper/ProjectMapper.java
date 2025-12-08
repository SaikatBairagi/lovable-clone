package com.apiorbit.lovableclone.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.apiorbit.lovableclone.dto.project.ProjectRequest;
import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProjectMapper {
    @Mapping(source = "user", target = "userProfile")
    ProjectResponse toProjectResponse(Project project);

    @Mapping(source = "user", target = "userProfile")
    List<ProjectSummeryResponse> toProjectSummeryResponse(List<Project> projects);
}
