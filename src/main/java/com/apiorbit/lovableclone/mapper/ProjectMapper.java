package com.apiorbit.lovableclone.mapper;

import com.apiorbit.lovableclone.dto.project.ProjectResponse;
import com.apiorbit.lovableclone.dto.project.ProjectSummeryResponse;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);


    List<ProjectSummeryResponse> toProjectSummeryResponse(List<Project> projects);

    @Mapping(source = "projectMember.user", target = "userProfile")
    @Mapping(source = "projectMember.memberRole", target="role")
    ProjectResponse toProjectResponse(Project project, ProjectMember  projectMember);
}
