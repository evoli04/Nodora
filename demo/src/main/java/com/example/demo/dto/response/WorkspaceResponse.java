package com.example.demo.dto.response;

import lombok.Data;

@Data
public class WorkspaceResponse {
    private Integer workspaceId; // setWorkspaceId() bunun için gerekli
    private Integer memberId;    // setMemberId() bunun için gerekli
    private String workspaceName;
    private Integer roleId;

    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public String getWorkspaceName() { return workspaceName; }
    public void setWorkspaceName(String workspaceName) { this.workspaceName = workspaceName; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
}
