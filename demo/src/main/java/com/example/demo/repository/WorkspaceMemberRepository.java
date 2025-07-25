package com.example.demo.repository;

import com.example.demo.model.workspace_members.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* @Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Integer> {
    List<WorkspaceMember> findByWorkspace_WorkspaceId(Integer workspaceId);
    Optional<WorkspaceMember> findByWorkspace_WorkspaceIdAndMember_MemberId(Integer workspaceId, Integer memberId);
}  */

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Integer> {
    List<WorkspaceMember> findByWorkspace_WorkspaceId(Integer workspaceId);
    Optional<WorkspaceMember> findByWorkspace_WorkspaceIdAndMember_MemberId(Integer workspaceId, Integer memberId);
}



