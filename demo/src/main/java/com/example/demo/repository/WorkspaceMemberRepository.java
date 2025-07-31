package com.example.demo.repository;

import com.example.demo.model.workspace_members.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Integer> {
    Optional<WorkspaceMember> findByWorkspace_WorkspaceIdAndMember_MemberId(Integer workspaceId, Integer memberId);
    List<WorkspaceMember> findByWorkspace_WorkspaceId(Integer workspaceId);
    List<WorkspaceMember> findByMember_MemberId(Integer memberId);
}