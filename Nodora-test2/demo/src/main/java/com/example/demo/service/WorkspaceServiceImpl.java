package com.example.demo.service;

import com.example.demo.dto.request.WorkspaceRequest;
import com.example.demo.dto.response.WorkspaceResponse;
import com.example.demo.model.roles.Roles;
import com.example.demo.model.workspaces.Workspaces;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.WorkspaceRepository;
import com.example.demo.repository.WorkspaceMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.model.workspace_members.WorkspaceMember;
import com.example.demo.model.workspaces.Workspaces;
import com.example.demo.model.members.Member;
import com.example.demo.model.roles.Roles;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceMemberRepository workspaceMemberRepository;


    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(WorkspaceRequest request) {
        // 1. Workspace oluştur
        Workspaces newWorkspace = new Workspaces(request.getMemberId(), request.getWorkspaceName());
        Workspaces savedWorkspace = workspaceRepository.save(newWorkspace);

        // 2. OWNER rolünü al
        Roles ownerRole = roleRepository.findByRoleName("OWNER")
                .orElseThrow(() -> new RuntimeException("OWNER rolü bulunamadı"));

        // 3. Workspace açan kullanıcıyı otomatik OWNER olarak workspace_member tablosuna ekle
        workspaceMemberService.addMember(new com.example.demo.dto.request.WorkspaceMemberRequest(
                savedWorkspace.getWorkspaceId(),
                request.getMemberId(),
                ownerRole.getRoleId()
        ));

        // 4. Response oluştur ve döndür
        WorkspaceResponse response = new WorkspaceResponse();
        response.setWorkspaceId(savedWorkspace.getWorkspaceId());
        response.setMemberId(savedWorkspace.getMemberId());
        response.setWorkspaceName(savedWorkspace.getWorkspaceName());
        response.setRoleId(ownerRole.getRoleId());
        return response;
    }


    @Override
    public List<WorkspaceResponse> getWorkspacesByMemberId(Integer memberId) {
        List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findByMember_MemberId(memberId);

        return workspaceMembers.stream()
                .map(wm -> {
                    WorkspaceResponse response = new WorkspaceResponse();
                    response.setWorkspaceId(wm.getWorkspace().getWorkspaceId());
                    response.setWorkspaceName(wm.getWorkspace().getWorkspaceName());
                    response.setMemberId(wm.getMember().getMemberId());
                    response.setRoleId(wm.getRole().getRoleId());
                    return response;
                })
                .collect(Collectors.toList());
    }
}