package com.example.demo.model.workspaces;

import jakarta.persistence.*;

@Entity
@Table(name = "workspaces")
public class Workspaces {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Integer workspace_id;

    @Column(name = "member_id", nullable = false)
    private Integer member_id;

    @Column(name = "workspace_name", length = 20, nullable = false)
    private String workspace_name;

    // Boş constructor (zorunlu)
    public Workspaces() {}

    // Parametreli constructor (isteğe bağlı)
    public Workspaces(Integer member_id, String workspace_name) {
        this.member_id = member_id;
        this.workspace_name = workspace_name;
    }

    // Getter ve Setter'lar

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

    public String getWorkspace_name() {
        return workspace_name;
    }

    public void setWorkspace_name(String workspace_name) {
        this.workspace_name = workspace_name;
    }

    // toString (isteğe bağlı)
    @Override
    public String toString() {
        return "Workspaces{" +
                "workspace_id=" + workspace_id +
                ", member_id=" + member_id +
                ", workspace_name='" + workspace_name + '\'' +
            '}';
}
}