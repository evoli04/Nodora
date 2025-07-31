package com.example.demo.controller;

import com.example.demo.dto.request.BoardRequest;
import com.example.demo.dto.response.BoardResponse;
import com.example.demo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    // Board oluşturma
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request) {
        try {
            // Service katmanında DTO'dan entity'ye mapleme yapılmalı
            BoardResponse response = boardService.createBoard(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Board güncelleme
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Integer boardId, @RequestBody BoardRequest request) {
        try {
            BoardResponse response = boardService.updateBoard(boardId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Board silme
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Integer boardId, @RequestParam Integer memberId) {
        try {
            boardService.deleteBoard(boardId, memberId);
            return ResponseEntity.ok("Board başarıyla silindi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Board'a lider atama
    @PostMapping("/{boardId}/promote-leader")
    public ResponseEntity<?> promoteLeader(@PathVariable Integer boardId,
                                           @RequestParam Integer memberId,
                                           @RequestParam Integer requesterId) {
        try {
            boardService.promoteLeader(boardId, memberId, requesterId);
            return ResponseEntity.ok("Lider başarıyla atandı.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}