package com.example.demo.service;

import com.example.demo.model.boards.Boards;
import com.example.demo.model.board_members.BoardMember;
import com.example.demo.model.roles.Roles;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.BoardMemberRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.WorkspaceMemberRepository;
import com.example.demo.model.workspace_members.WorkspaceMember;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.example.demo.dto.request.BoardRequest;
import com.example.demo.dto.response.BoardResponse;
import jakarta.persistence.Column;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardMemberRepository boardMemberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WorkspaceMemberRepository workspaceMemberRepository;

    /**
     * Sadece workspace owner (role_id=2) board oluşturabilir.
     * Oluşturan otomatik olarak board leader (role_id=3) olur.
     */
    @Transactional
    public Boards createBoard(Boards board, Integer creatorMemberId, Integer creatorRoleId) {
        if (creatorRoleId != 2) {
            throw new RuntimeException("Sadece workspace owner board oluşturabilir!");
        }
        Boards savedBoard = boardRepository.save(board);
        // BoardMember kaydı: owner otomatik olarak leader (role_id=3) olur
        BoardMember leader = new BoardMember();
        leader.setBoard(savedBoard);
        // Yeni yapıda memberId doğrudan Board entity'sinde var
        leader.setMember(memberRepository.findById(board.getMemberId()).orElseThrow());
        leader.setRoleId(3); // leader
        // workspaceMember'ı set et
        WorkspaceMember workspaceMember = workspaceMemberRepository
            .findByWorkspace_WorkspaceIdAndMember_MemberId(board.getWorkspaceId(), board.getMemberId())
            .orElseThrow(() -> new RuntimeException("Owner workspaceMember olarak bulunamadı!"));
        leader.setWorkspaceMember(workspaceMember);
        boardMemberRepository.save(leader);
        logAction(creatorMemberId, "BOARD_CREATE", savedBoard.getBoardId());
        return savedBoard;
    }

    public BoardResponse createBoard(BoardRequest request) {
        Boards board = new Boards();
        board.setWorkspaceId(request.getWorkspaceId());
        board.setBgColor(request.getBgColor());
        board.setMemberId(request.getMemberId());
        board.setTitle(request.getTitle());
        // Role mapping
        Roles role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role bulunamadı!"));
        board.setRole(role);
        Boards saved = createBoard(board, request.getMemberId(), request.getRoleId());
        BoardResponse response = new BoardResponse();
        response.setBoardId(saved.getBoardId());
        response.setWorkspaceId(saved.getWorkspaceId());
        response.setBgColor(saved.getBgColor());
        response.setMemberId(saved.getMemberId());
        response.setTitle(saved.getTitle());
        response.setRoleId(saved.getRole().getRoleId());
        return response;
    }

    /**
     * Sadece board leader (role_id=3) board'u güncelleyebilir.
     */
    @Transactional
    public Boards updateBoard(Integer boardId, Boards updatedBoard, Integer updaterMemberId) {
        Optional<BoardMember> bmOpt = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, updaterMemberId);
        if (bmOpt.isEmpty() || bmOpt.get().getRoleId() != 3) {
            throw new RuntimeException("Sadece board leader güncelleme yapabilir!");
        }
        Boards board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board bulunamadı!"));
        board.setTitle(updatedBoard.getTitle());
        board.setBgColor(updatedBoard.getBgColor());
        // Diğer güncellenebilir alanlar eklenebilir
        Boards saved = boardRepository.save(board);
        logAction(updaterMemberId, "BOARD_UPDATE", boardId);
        return saved;
    }

    public BoardResponse updateBoard(Integer boardId, BoardRequest request) {
        Boards board = new Boards();
        board.setWorkspaceId(request.getWorkspaceId());
        board.setBgColor(request.getBgColor());
        board.setMemberId(request.getMemberId());
        board.setTitle(request.getTitle());
        Roles role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role bulunamadı!"));
        board.setRole(role);
        Boards updated = updateBoard(boardId, board, request.getMemberId());
        BoardResponse response = new BoardResponse();
        response.setBoardId(updated.getBoardId());
        response.setWorkspaceId(updated.getWorkspaceId());
        response.setBgColor(updated.getBgColor());
        response.setMemberId(updated.getMemberId());
        response.setTitle(updated.getTitle());
        response.setRoleId(updated.getRole().getRoleId());
        return response;
    }

    /**
     * Sadece owner (role_id=2) veya leader (role_id=3) board'u silebilir.
     * Board silinince tüm alt nesneler de silinir (cascade).
     */
    @Transactional
    public void deleteBoard(Integer boardId, Integer deleterMemberId) {
        Optional<BoardMember> bmOpt = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, deleterMemberId);
        if (bmOpt.isEmpty() || !(bmOpt.get().getRoleId() == 2 || bmOpt.get().getRoleId() == 3)) {
            throw new RuntimeException("Sadece owner veya leader board silebilir!");
        }
        boardRepository.deleteById(boardId);
        logAction(deleterMemberId, "BOARD_DELETE", boardId);
    }

    /**
     * Board'a yeni lider atama işlemi. Sadece owner yapabilir.
     * Board'da sadece bir lider olabilir. Owner'ın rolünü member'a (5) düşür.
     */
    @Transactional
    public void assignLeader(Integer boardId, Integer ownerId, Integer newLeaderMemberId) {
        Optional<BoardMember> ownerBM = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, ownerId);
        if (ownerBM.isEmpty() || ownerBM.get().getRoleId() != 2) {
            throw new RuntimeException("Sadece owner lider atayabilir!");
        }
        // Mevcut lider varsa, rolünü member'a (5) düşür
        Optional<BoardMember> currentLeader = boardMemberRepository.findLeaderByBoard(boardId);
        currentLeader.ifPresent(leader -> {
            leader.setRoleId(5); // Eski liderin rolü 5 (member) olur
            boardMemberRepository.save(leader);
        });
        // Yeni lideri ata (yoksa ekle)
        Optional<BoardMember> newLeaderOpt = boardMemberRepository.findByBoard_BoardIdAndMember_MemberId(boardId, newLeaderMemberId);
        BoardMember newLeader;
        if (newLeaderOpt.isPresent()) {
            newLeader = newLeaderOpt.get();
            newLeader.setRoleId(3);
        } else {
            newLeader = new BoardMember();
            newLeader.setBoard(boardRepository.findById(boardId).orElseThrow());
            // Member ve workspaceMember set edilmeli!
            // newLeader.setWorkspaceMember(...);
            // newLeader.setMember(...);
            newLeader.setRoleId(3);
        }
        boardMemberRepository.save(newLeader);
        // Owner'ın rolünü member'a (5) düşür
        BoardMember owner = ownerBM.get();
        owner.setRoleId(5); // Owner'ın rolü 5 (member) olur
        boardMemberRepository.save(owner);
        logAction(ownerId, "ASSIGN_LEADER", boardId);
    }

    /**
     * Board'a workspaceMember'da bulunan bir üyeyi ekler. Sadece workspaceMember'da olanlar eklenebilir.
     * Eklenen üyenin rolü 5 (member) olur.
     */
    @Transactional
    public void addMemberToBoard(Integer boardId, Integer workspaceId, Integer memberId) {
        // WorkspaceMember kaydını bul
        WorkspaceMember workspaceMember = workspaceMemberRepository
            .findByWorkspace_WorkspaceIdAndMember_MemberId(workspaceId, memberId)
            .orElseThrow(() -> new RuntimeException("Bu üye workspace'e ait değil!"));
        Boards board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("Board bulunamadı!"));
        // Zaten board üyesi mi kontrol et
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

    // Basit log mekanizması (ileride ayrı tabloya yazılabilir)
    private void logAction(Integer memberId, String action, Integer boardId) {
        System.out.println("LOG: memberId=" + memberId + ", action=" + action + ", boardId=" + boardId);
    }
} 