package com.example.demo.model.checklist_items;

import jakarta.persistence.*;

@Entity
@Table(name = "checklist_items")
public class Checklist_Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_items_id")
    private Integer checklistItemsId;

    @Column(name = "checklist_id", nullable = false)
    private Integer checklistId;

    @Column(name = "text", length = 25, nullable = false)
    private String text;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(name = "position", nullable = false)
    private Integer position;

    // Getter ve Setter'lar
    public Integer getChecklistItemsId() { return checklistItemsId; }
    public void setChecklistItemsId(Integer checklistItemsId) { this.checklistItemsId = checklistItemsId; }

    public Integer getChecklistId() { return checklistId; }
    public void setChecklistId(Integer checklistId) { this.checklistId = checklistId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position=position;}
}