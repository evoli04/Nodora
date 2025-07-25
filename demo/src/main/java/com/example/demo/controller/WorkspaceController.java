package com.example.demo.controller;

import com.example.demo.dto.request.WorkspaceRequest;
import com.example.demo.dto.response.WorkspaceResponse;
import com.example.demo.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // Yeni bir workspace oluştur
    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(@Valid @RequestBody WorkspaceRequest request) {
        WorkspaceResponse createdWorkspace = workspaceService.createWorkspace(request);
        return ResponseEntity.ok(createdWorkspace);
    }

    // Belirli bir kullanıcıya ait workspaceleri getir
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<WorkspaceResponse>> getWorkspacesByMember(@PathVariable Integer memberId) {
        List<WorkspaceResponse> workspaces = workspaceService.getWorkspacesByMemberId(memberId);
        return ResponseEntity.ok(workspaces);
    }
}
