package com.example.demo.service;

import com.example.demo.dto.request.WorkspaceMemberRequest;
import com.example.demo.dto.response.WorkspaceMemberResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.members.Member;
import com.example.demo.model.roles.Roles;
import com.example.demo.model.workspace_members.WorkspaceMember;
import com.example.demo.model.workspaces.Workspaces;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.WorkspaceRepository;
import com.example.demo.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public WorkspaceMemberResponse addMember(WorkspaceMemberRequest request) {
        Workspaces workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace bulunamadı"));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Üye bulunamadı"));

        Roles role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol bulunamadı"));

        WorkspaceMember wm = new WorkspaceMember();
        wm.setWorkspace(workspace);
        wm.setMember(member);
        wm.setRole(role);
        wm.setCreatedAt(LocalDateTime.now());

        WorkspaceMember saved = workspaceMemberRepository.save(wm);

        return convertToResponse(saved);
    }

   /* @Override
    public List<WorkspaceMemberResponse> getMembersByWorkspaceId(Integer workspaceId) {
        // Burada repository’deki methodu kullandık:
        List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspace_Id(workspaceId);
        return members.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }  */
   @Override
   public List<WorkspaceMemberResponse> getMembersByWorkspaceId(Integer workspaceId) {
       List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspace_WorkspaceId(workspaceId);
       return members.stream()
               .map(this::convertToResponse)
               .collect(Collectors.toList());
   }


    @Override
    public WorkspaceMemberResponse updateMemberRole(Integer workspaceMemberId, Integer newRoleId) {
        WorkspaceMember wm = workspaceMemberRepository.findById(workspaceMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace üyesi bulunamadı"));

        Roles role = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol bulunamadı"));

        wm.setRole(role);
        WorkspaceMember updated = workspaceMemberRepository.save(wm);

        return convertToResponse(updated);
    }

    @Override
    public void removeMember(Integer workspaceMemberId) {
        WorkspaceMember wm = workspaceMemberRepository.findById(workspaceMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace üyesi bulunamadı"));
        workspaceMemberRepository.delete(wm);
    }

    private WorkspaceMemberResponse convertToResponse(WorkspaceMember wm) {
        WorkspaceMemberResponse response = new WorkspaceMemberResponse();
        response.setWorkspaceMemberId(wm.getId()); // workspace member id

        // Workspaces entity id getter'ı bilmiyoruz, varsayalım workspaceId:
        response.setWorkspaceId(wm.getWorkspace().getWorkspaceId());

        response.setMemberId(wm.getMember().getMemberId());
        response.setRoleId(wm.getRole().getRoleId());
        return response;
    }

}
