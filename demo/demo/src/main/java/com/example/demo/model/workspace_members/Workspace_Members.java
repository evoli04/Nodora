package com.example.demo.model.workspace_members;

import java.util.Objects;

public class Workspace_Members {
    private Integer id;
    private Integer workspaceId;
    private Integer memberId;
    private Integer roleId; // roles_id yerine roleId kullanıldı

    // No-args constructor
    public Workspace_Members() {
    }

    // All-args constructor
    public Workspace_Members(Integer id, Integer workspaceId, Integer memberId, Integer roleId) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.memberId = memberId;
        this.roleId = roleId;
    }

    // Getter ve Setter metodları
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    // toString metodu
    @Override
    public String toString() {
        return "WorkspaceMember{" +
                "id=" + id +
                ", workspaceId=" + workspaceId +
                ", memberId=" + memberId +
                ", roleId=" + roleId +
                '}';
    }

    // equals ve hashCode metodları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspace_Members that = (Workspace_Members) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(workspaceId, that.workspaceId) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workspaceId, memberId, roleId);
    }
}