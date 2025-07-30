package com.example.demo.controller;

import com.example.demo.model.board_members.BoardMember;
import com.example.demo.service.BoardMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-members")
public class BoardMemberController {
    @Autowired
    private BoardMemberService boardMemberService;

    @PostMapping("/add")
    public ResponseEntity<?> addMemberToBoard(@RequestParam Integer boardId, @RequestParam Integer workspaceId, @RequestParam Integer memberId, @RequestParam Integer requesterId) {
        boardMemberService.addMemberToBoard(boardId, workspaceId, memberId, requesterId);
        return ResponseEntity.ok("Üye board'a eklendi.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeMemberFromBoard(@RequestParam Integer boardId, @RequestParam Integer memberId, @RequestParam Integer requesterId) {
        boardMemberService.removeMemberFromBoard(boardId, memberId, requesterId);
        return ResponseEntity.ok("Üye board'dan çıkarıldı.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardMember>> getBoardMembers(@RequestParam Integer boardId) {
        return ResponseEntity.ok(boardMemberService.getBoardMembers(boardId));
    }

    @PostMapping("/promote-leader")
    public ResponseEntity<?> promoteToLeader(@RequestParam Integer boardId, @RequestParam Integer memberId, @RequestParam Integer requesterId) {
        boardMemberService.promoteToLeader(boardId, memberId, requesterId);
        return ResponseEntity.ok("Üye lider olarak atandı.");
    }
} 