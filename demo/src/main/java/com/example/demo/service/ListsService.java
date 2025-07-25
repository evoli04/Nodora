package com.example.demo.service;

import com.example.demo.dto.request.ListsRequest;
import com.example.demo.dto.response.ListsResponse;
import com.example.demo.model.lists.Lists;
import com.example.demo.repository.ListsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListsService {

    @Autowired
    private ListsRepository listsRepository;

    // Tüm listeleri getirir (entity)
    public List<Lists> getAllLists() {
        return listsRepository.findAll();
    }

    // Tüm listeleri getirir (response DTO)
    public List<ListsResponse> getAllListsResponse() {
        return listsRepository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    // ID'ye göre bir listeyi getirir (entity)
    public Lists getListById(Integer id) {
        Optional<Lists> optional = listsRepository.findById(id);
        return optional.orElse(null);
    }

    // ID'ye göre bir listeyi getirir (response DTO)
    public ListsResponse getListResponseById(Integer id) {
        Optional<Lists> optional = listsRepository.findById(id);
        return optional.map(this::toResponseDto).orElse(null);
    }

    // Yeni bir liste oluşturur (entity)
    public Lists createList(Lists lists, Integer memberId) {
        lists.setMemberId(memberId);
        return listsRepository.save(lists);
    }

    // Yeni bir liste oluşturur (request DTO)
    public ListsResponse createListResponse(ListsRequest listsRequest, Integer memberId) {
        Lists lists = new Lists();
        lists.setTitle(listsRequest.getTitle());
        lists.setPosition(String.valueOf(listsRequest.getPosition()));
        lists.setBoardId(listsRequest.getBoard_id());
        lists.setMemberId(memberId);
        Lists saved = listsRepository.save(lists);
        return toResponseDto(saved);
    }

    // Var olan bir listeyi günceller (entity)
    public Lists updateList(Integer id, Lists lists) {
        Optional<Lists> optional = listsRepository.findById(id);
        if (optional.isPresent()) {
            Lists existing = optional.get();
            existing.setTitle(lists.getTitle());
            existing.setPosition(lists.getPosition());
            existing.setBoardId(lists.getBoardId());
            return listsRepository.save(existing);
        } else {
            return null;
        }
    }

    // Var olan bir listeyi günceller (request DTO)
    public ListsResponse updateListResponse(Integer id, ListsRequest listsRequest) {
        Optional<Lists> optional = listsRepository.findById(id);
        if (optional.isPresent()) {
            Lists existing = optional.get();
            existing.setTitle(listsRequest.getTitle());
            existing.setPosition(String.valueOf(listsRequest.getPosition()));
            existing.setBoardId(listsRequest.getBoard_id());
            Lists updated = listsRepository.save(existing);
            return toResponseDto(updated);
        } else {
            return null;
        }
    }

    // Belirli bir listeyi siler
    public boolean deleteList(Integer id) {
        if (listsRepository.existsById(id)) {
            listsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Entity'den Response DTO'ya dönüşüm
    private ListsResponse toResponseDto(Lists entity) {
        ListsResponse dto = new ListsResponse();
        dto.setList_id(entity.getListId());
        dto.setTitle(entity.getTitle());
        dto.setBoard_id(entity.getBoardId());
        try {
            dto.setPosition(Integer.valueOf(entity.getPosition()));
        } catch (Exception e) {
            dto.setPosition(null);
        }
        return dto;
}
}
