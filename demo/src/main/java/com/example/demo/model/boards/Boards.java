package com.example.demo.model.boards;

import jakarta.persistence.*;

@Entity
@Table(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "workspace_id", nullable = false)
    private Integer workspaceId;

    @Column(name = "bg_color", length = 15)
    private String bgColor;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "title", length = 15, nullable = false)
    private String title;

    // Getter ve Setter'lar
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }

    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title=title;}
}