package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.member.InviteMemberRequest;
import com.apiorbit.lovableclone.dto.member.MemberResponse;
import com.apiorbit.lovableclone.dto.member.UpdateMemberRole;
import com.apiorbit.lovableclone.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("NullableProblems")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long projectId) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectMemberService.getAllMembers(userId, projectId));

    }

    @PostMapping("/invite")
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @RequestBody InviteMemberRequest memberRequest) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectMemberService.inviteMember(projectId, userId, memberRequest));
    }

    @PatchMapping("/updateRole/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRole memberRole) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectMemberService.updateMemberRole(projectId, userId, memberId, memberRole));
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId) {
        Long userId = 1L;
        return ResponseEntity.ok().body(projectMemberService.deleteMember(projectId, userId, memberId));
    }


}
