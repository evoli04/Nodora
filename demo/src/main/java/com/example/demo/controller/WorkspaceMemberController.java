package com.example.demo.controller;

import com.example.demo.dto.request.WorkspaceMemberRequest;
import com.example.demo.dto.response.WorkspaceMemberResponse;
import com.example.demo.service.WorkspaceMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspace-members")
public class WorkspaceMemberController {

    @Autowired
    private WorkspaceMemberService workspaceMemberService;

    @PostMapping("/invite")
    public WorkspaceMemberResponse inviteMember(@RequestBody WorkspaceMemberRequest request) {
        return workspaceMemberService.addMember(request);
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<WorkspaceMemberResponse> getMembers(@PathVariable Integer workspaceId) {
        return workspaceMemberService.getMembersByWorkspaceId(workspaceId);
    }

    @PutMapping("/{id}/role")
    public WorkspaceMemberResponse updateRole(@PathVariable Integer id, @RequestParam Integer newRoleId) {
        return workspaceMemberService.updateMemberRole(id, newRoleId);
    }

    @DeleteMapping("/{id}")
    public void removeMember(@PathVariable Integer id) {
        workspaceMemberService.removeMember(id);
    }

    @GetMapping("/member/{memberId}")
    public List<WorkspaceMemberResponse> getWorkspaces(@PathVariable Integer memberId) {
        return workspaceMemberService.getWorkspacesByMemberId(memberId);
    }
}