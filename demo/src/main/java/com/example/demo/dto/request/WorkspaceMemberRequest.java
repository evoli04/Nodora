package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkspaceMemberRequest {

    @NotNull(message = "Workspace ID boş olamaz")
    private Integer workspaceId;

    @NotNull(message = "Member ID boş olamaz")
    private Integer memberId;

    @NotNull(message = "Role ID boş olamaz")
    private Integer roleId;

    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
}
