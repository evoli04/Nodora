package com.example.demo.dto.response;

import lombok.Data;

@Data
public class WorkspaceMemberResponse {

    private Integer workspaceMemberId;
    private Integer workspaceId;
    private Integer memberId;
    private Integer roleId;

    public Integer getWorkspaceMemberId() { return workspaceMemberId; }
    public void setWorkspaceMemberId(Integer workspaceMemberId) { this.workspaceMemberId = workspaceMemberId; }
    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
}
