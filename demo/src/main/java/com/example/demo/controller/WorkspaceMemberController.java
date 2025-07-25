package com.example.demo.controller;

import com.example.demo.dto.request.WorkspaceMemberRequest;
import com.example.demo.dto.response.WorkspaceMemberResponse;
import com.example.demo.service.WorkspaceMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspace-members")
@RequiredArgsConstructor
public class WorkspaceMemberController {

    private final WorkspaceMemberService service;

    @PostMapping
    public ResponseEntity<WorkspaceMemberResponse> addMember(@Valid @RequestBody WorkspaceMemberRequest request) {
        WorkspaceMemberResponse response = service.addMember(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<List<WorkspaceMemberResponse>> getMembersByWorkspace(@PathVariable Integer workspaceId) {
        List<WorkspaceMemberResponse> list = service.getMembersByWorkspaceId(workspaceId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<WorkspaceMemberResponse> updateRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        WorkspaceMemberResponse response = service.updateMemberRole(id, roleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable Integer id) {
        service.removeMember(id);
        return ResponseEntity.noContent().build();
    }
}
