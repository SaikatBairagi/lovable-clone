package com.apiorbit.lovableclone.mapper;

import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;
import com.apiorbit.lovableclone.dto.member.MemberResponse;
import com.apiorbit.lovableclone.entity.Project;
import com.apiorbit.lovableclone.entity.ProjectMember;
import com.apiorbit.lovableclone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberResponseMapper {

    @Mapping(expression = "java(user.getFirstName() + \" \" + user.getLastName())", target = "name")
    MemberResponse toMemberResponse(User user);

    @Mapping(expression = "java(user.getFirstName() + \" \" + user.getLastName())", target = "name")
    @Mapping(source = "projectMember.memberRole", target = "role")
    MemberResponse toMemberResponse(User user, ProjectMember  projectMember);

    UserProfileResponse toUserProfileResponse(User user);
}
