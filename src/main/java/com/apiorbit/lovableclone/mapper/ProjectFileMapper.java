package com.apiorbit.lovableclone.mapper;

import com.apiorbit.lovableclone.dto.project.FileNode;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toFileNode(List<ProjectFile> projectFiles);
}
