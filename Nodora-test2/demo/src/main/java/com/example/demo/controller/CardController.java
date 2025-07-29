package com.example.demo.controller;

import com.example.demo.dto.request.CardRequest;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.model.cards.Card;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest request) {
        Card card = toEntity(request);
        Card saved = cardService.createCard(card);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Integer id) {
        return cardService.getCardById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<CardResponse> responses = cardService.getAllCards()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Integer id, @RequestBody CardRequest request) {
        Card card = toEntity(request);
        Card updated = cardService.updateCard(id, card);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    // --- Dönüştürücü Yardımcı Metotlar ---
    private Card toEntity(CardRequest request) {
        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setEndingDate(request.getEndingDate());
        card.setListId(request.getListId());
        card.setPosition(request.getPosition());
        card.setMemberId(request.getMemberId());
        return card;
    }

    private CardResponse toResponse(Card card) {
        CardResponse response = new CardResponse();
        response.setCardId(card.getCardId());
        response.setTitle(card.getTitle());
        response.setDescription(card.getDescription());
        response.setEndingDate(card.getEndingDate());
        response.setListId(card.getListId());
        response.setPosition(card.getPosition());
        response.setMemberId(card.getMemberId());
        return response;
  }
}