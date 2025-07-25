package com.example.demo.controller;

import com.example.demo.dto.request.CardLabelsRequest;
import com.example.demo.dto.response.CardLabelsResponse;
import com.example.demo.service.CardLabelsService;
import com.example.demo.validation.CardLabelsValidation;
import com.example.demo.exception.CardLabelNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardlabels")
public class CardLabelsController {

    @Autowired
    private CardLabelsService cardLabelsService;

    @Autowired
    private CardLabelsValidation cardLabelsValidation;

    // Tüm CardLabel kayıtlarını getir
    @GetMapping
    public ResponseEntity<List<CardLabelsResponse>> getAll() {
        return ResponseEntity.ok(cardLabelsService.getAllCardLabels());
    }

    // ID ile CardLabel getir
    @GetMapping("/{id}")
    public ResponseEntity<CardLabelsResponse> getById(@PathVariable Integer id) {
        var result = cardLabelsService.getCardLabelById(id);
        if (result == null) throw new CardLabelNotFoundException(id);
        return ResponseEntity.ok(result);
    }

    // Yeni CardLabel ekle
    @PostMapping
    public ResponseEntity<CardLabelsResponse> create(@RequestBody CardLabelsRequest dto) {
        cardLabelsValidation.validate(dto);
        return ResponseEntity.ok(cardLabelsService.createCardLabel(dto));
    }

    // CardLabel güncelle
    @PutMapping("/{id}")
    public ResponseEntity<CardLabelsResponse> update(@PathVariable Integer id, @RequestBody CardLabelsRequest dto) {
        cardLabelsValidation.validate(dto);
        var updated = cardLabelsService.updateCardLabel(id, dto);
        if (updated == null) throw new CardLabelNotFoundException(id);
        return ResponseEntity.ok(updated);
    }

    // CardLabel sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = cardLabelsService.deleteCardLabel(id);
        if (!deleted) throw new CardLabelNotFoundException(id);
        return ResponseEntity.noContent().build();
   }
}