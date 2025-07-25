package com.example.demo.controller;

import com.example.demo.dto.request.ListsRequest;
import com.example.demo.dto.response.ListsResponse;
import com.example.demo.model.lists.Lists;
import com.example.demo.service.ListsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListsController {

    private final ListsService listsService;

    // Liste oluşturma
    @PostMapping
    public ResponseEntity<ListsResponse> createList(@RequestBody ListsRequest request, @RequestParam Integer memberId) {
        ListsResponse response = listsService.createListResponse(request, memberId);
        return ResponseEntity.ok(response);
    }

    // Tüm listeleri getir
    @GetMapping
    public ResponseEntity<List<ListsResponse>> getAllLists() {
        List<ListsResponse> responses = listsService.getAllListsResponse();
        return ResponseEntity.ok(responses);
    }

    // ID'ye göre liste getir
    @GetMapping("/{id}")
    public ResponseEntity<ListsResponse> getListById(@PathVariable Integer id) {
        ListsResponse response = listsService.getListResponseById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Liste güncelleme (kim güncelledi: memberId)
    @PutMapping("/{id}")
    public ResponseEntity<ListsResponse> updateList(@PathVariable Integer id, @RequestBody ListsRequest request, @RequestParam Integer memberId) {
        // Güncelleyenin id'sini kaydetmek için modelde memberId güncellenir
        ListsResponse response = listsService.updateListResponse(id, request);
        if (response != null) {
            // Güncelleyenin id'sini de güncelle
            Lists entity = listsService.getListById(id);
            if (entity != null) {
                entity.setMemberId(memberId);
                listsService.updateList(id, entity);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Liste silme (kim sildi: memberId)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteList(@PathVariable Integer id, @RequestParam Integer memberId) {
        // Silen kişinin id'si loglanabilir veya başka bir işlem yapılabilir
        boolean deleted = listsService.deleteList(id);
        if (deleted) {
            return ResponseEntity.ok("List silindi. Silen üye id: " + memberId);
        } else {
            return ResponseEntity.notFound().build();
 }
}
}