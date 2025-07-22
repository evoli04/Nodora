package com.example.demo.model.workspace_members;

import jakarta.persistence.*;

@Entity
@Table(name = "workspace_members")
public class Workspace_Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_member_id")
    private Integer workspace_member_id;

    @Column(name = "workspace_id", nullable = false)
    private Integer workspace_id;

    @Column(name = "member_id", nullable = false)
    private Integer member_id;

    @Column(name = "role_id", nullable = false)
    private Integer role_id;

    // Boş constructor (zorunlu)
    public Workspace_Members() {}

    // Parametreli constructor (isteğe bağlı)
    public Workspace_Members(Integer workspace_id, Integer member_id, Integer role_id) {
        this.workspace_id = workspace_id;
        this.member_id = member_id;
        this.role_id = role_id;
    }

    // Getter ve Setter'lar

    public Integer getWorkspace_member_id() {
        return workspace_member_id;
    }

    public void setWorkspace_member_id(Integer workspace_member_id) {
        this.workspace_member_id = workspace_member_id;
    }

    public Integer getWorkspace_id() {
        return workspace_id;
    }

    public void setWorkspace_id(Integer workspace_id) {
        this.workspace_id = workspace_id;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    // toString metodu (isteğe bağlı)
    @Override
    public String toString() {
        return "Workspace_Members{" +
                "workspace_member_id=" + workspace_member_id +
                ", workspace_id=" + workspace_id +
                ", member_id=" + member_id +
                ", role_id=" + role_id +
            '}';
}
}