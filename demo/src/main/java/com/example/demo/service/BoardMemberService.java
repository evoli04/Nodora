package com.example.demo.service;

import com.example.demo.model.board_members.BoardMember;
import java.util.List;

public interface BoardMemberService {
    void addMemberToBoard(Integer boardId, Integer workspaceId, Integer memberId, Integer requesterId);
    void removeMemberFromBoard(Integer boardId, Integer memberId, Integer requesterId);
    List<BoardMember> getBoardMembers(Integer boardId);
    void promoteToLeader(Integer boardId, Integer memberId, Integer requesterId);
} 