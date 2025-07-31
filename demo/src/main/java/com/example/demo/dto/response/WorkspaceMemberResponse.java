package com.example.demo.dto.response;

import java.time.LocalDateTime;

public class WorkspaceMemberResponse {

    private Integer workspaceMemberId;
    private Integer workspaceId;
    private String workspaceName;
    private Integer memberId;
    private String memberName;
    private String memberEmail;
    private Integer roleId;
    private String roleName;
    private LocalDateTime createdAt;

    public WorkspaceMemberResponse() {
    }

    public Integer getWorkspaceMemberId() {
        return workspaceMemberId;
    }

    public void setWorkspaceMemberId(Integer workspaceMemberId) {
        this.workspaceMemberId = workspaceMemberId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}