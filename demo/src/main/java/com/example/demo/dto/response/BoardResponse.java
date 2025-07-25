package com.example.demo.dto.response;

public class BoardResponse {
    private Integer boardId;
    private Integer workspaceId;
    private String bgColor;
    private String title;
    private Integer roleId;
    private Integer memberId;

    public Integer getBoardId() { return boardId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }
    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
} 