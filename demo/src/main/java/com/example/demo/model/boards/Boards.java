package com.example.demo.model.boards;

import com.example.demo.model.board_members.BoardMember;
import com.example.demo.model.members.Member;
import com.example.demo.model.roles.Roles;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @Column(name = "workspace_id", nullable = false)
    private Integer workspaceId;

    @Column(name = "bg_color", length = 15, nullable = false)
    private String bgColor;

    @Column(name = "title", length = 15, nullable = false)
    private String title;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Roles role;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers;

    // Getter ve Setter'lar
    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(Integer workspaceId) { this.workspaceId = workspaceId; }

    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public List<BoardMember> getBoardMembers() { return boardMembers; }
    public void setBoardMembers(List<BoardMember> boardMembers) { this.boardMembers = boardMembers; }
}