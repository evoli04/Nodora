package com.example.demo.model.boards;

import java.util.Objects;

public class Boards {
    private Integer id;
    private Integer workspaceId;
    private String bgColor; // bg_color
    private Integer createdBy; // created_by
    private String title;

    // No-args constructor
    public Boards() {
    }

    // All-args constructor
    public Boards(Integer id, Integer workspaceId, String bgColor, Integer createdBy, String title) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.bgColor = bgColor;
        this.createdBy = createdBy;
        this.title = title;
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

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // toString metodu
    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", workspaceId=" + workspaceId +
                ", bgColor='" + bgColor + '\'' +
                ", createdBy=" + createdBy +
                ", title='" + title + '\'' +
                '}';
    }

    // equals ve hashCode metodları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boards board = (Boards) o;
        return Objects.equals(id, board.id) &&
                Objects.equals(workspaceId, board.workspaceId) &&
                Objects.equals(bgColor, board.bgColor) &&
                Objects.equals(createdBy, board.createdBy) &&
                Objects.equals(title, board.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workspaceId, bgColor, createdBy, title);
    }
}