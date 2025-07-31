package com.example.demo.dto.request;

public class WorkspaceMemberRequest {
    private Integer workspaceId;
    private String memberEmail; // Davet edilen kişinin e-postası
    private Integer roleId;

    public WorkspaceMemberRequest(Integer workspaceId, Integer memberId, Integer roleId) {
        this.workspaceId = workspaceId;
        this.memberEmail = memberEmail;
        this.roleId = roleId;
    }


    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
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
}