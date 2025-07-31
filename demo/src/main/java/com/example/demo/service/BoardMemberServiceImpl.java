package com.example.demo.service;

import com.example.demo.model.board_members.BoardMember;
import com.example.demo.model.boards.Boards;
import com.example.demo.model.workspace_members.WorkspaceMember;
import com.example.demo.repository.BoardMemberRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.WorkspaceMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardMemberServiceImpl implements BoardMemberService {

    @Autowired
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    private BoardService boardService; // BoardService eklendi

    @Override
    @Transactional
    public void addMemberToBoard(Integer boardId, Integer workspaceId, Integer memberId, Integer requesterId) {
        // Sadece board lideri ekleyebilir
        BoardMember requester = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, requesterId)
                .orElseThrow(() -> new RuntimeException("İsteği yapan board üyesi değil!"));
        if (requester.getRoleId() != 3) {
            throw new RuntimeException("Sadece board lideri üye ekleyebilir!");
        }
        WorkspaceMember workspaceMember = workspaceMemberRepository
                .findByWorkspace_WorkspaceIdAndMember_MemberId(workspaceId, memberId)
                .orElseThrow(() -> new RuntimeException("Bu üye workspace'e ait değil!"));
        Boards board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board bulunamadı!"));
        if (boardMemberRepository.existsByBoard_BoardIdAndMember_MemberId(boardId, memberId)) {
            throw new RuntimeException("Bu üye zaten board üyesi!");
        }
        BoardMember boardMember = new BoardMember();
        boardMember.setBoard(board);
        boardMember.setMember(workspaceMember.getMember());
        boardMember.setWorkspaceMember(workspaceMember);
        boardMember.setRoleId(5); // member
        boardMemberRepository.save(boardMember);
    }
    @Override
    @Transactional
    public void removeMemberFromBoard(Integer boardId, Integer memberId, Integer requesterId) {
        BoardMember requester = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, requesterId)
                .orElseThrow(() -> new RuntimeException("İsteği yapan board üyesi değil!"));
        if (requester.getRoleId() != 3) {
            throw new RuntimeException("Sadece board lideri üye çıkarabilir!");
        }
        boardMemberRepository.deleteByBoard_BoardIdAndMember_MemberId(boardId, memberId);
    }

    @Override
    public List<BoardMember> getBoardMembers(Integer boardId) {
        return boardMemberRepository.findAllByBoardId(boardId);
    }

    @Override
    @Transactional
    public void promoteToLeader(Integer boardId, Integer memberId, Integer requesterId) {
        // Yetkiyi doğrudan BoardService'e devrediyoruz
        boardService.promoteLeader(boardId, memberId, requesterId);
    }
}