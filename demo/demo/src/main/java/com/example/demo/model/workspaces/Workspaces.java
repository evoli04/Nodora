package com.example.demo.model.workspaces;

import java.util.Objects;

public class Workspaces {
    private Integer id; // IDENTITY olduğu için Integer
    private String name; // varchar(20)
    private Integer ownerId; // owner_id

    // No-args constructor
    public Workspaces() {
    }

    // All-args constructor
    public Workspaces(Integer id, String name, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    // Getter ve Setter metodları
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    // toString metodu
    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }

    // equals ve hashCode metodları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspaces workspace = (Workspaces) o;
        return Objects.equals(id, workspace.id) &&
                Objects.equals(name, workspace.name) &&
                Objects.equals(ownerId, workspace.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId);
    }
}