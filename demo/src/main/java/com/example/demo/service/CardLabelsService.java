package com.example.demo.service;

import com.example.demo.dto.request.CardLabelsRequest;
import com.example.demo.dto.response.CardLabelsResponse;
import com.example.demo.model.card_labels.Card_Labels;
import com.example.demo.repository.CardLabelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardLabelsService {
    @Autowired
    private CardLabelsRepository cardLabelsRepository;

    // Tüm card label'ları getir
    public List<CardLabelsResponse> getAllCardLabels() {
        return cardLabelsRepository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    // ID'ye göre card label getir
    public CardLabelsResponse getCardLabelById(Integer id) {
        Optional<Card_Labels> cardLabel = cardLabelsRepository.findById(id);
        return cardLabel.map(this::toResponseDto).orElse(null);
    }

    // Yeni card label oluştur
    public CardLabelsResponse createCardLabel(CardLabelsRequest dto) {
        Card_Labels entity = new Card_Labels();
        entity.setCardId(dto.getCardId());
        entity.setLabelId(dto.getLabelId());
        entity.setMemberId(dto.getMemberId());
        Card_Labels saved = cardLabelsRepository.save(entity);
        return toResponseDto(saved);
    }

    // Card label güncelle
    public CardLabelsResponse updateCardLabel(Integer id, CardLabelsRequest dto) {
        Optional<Card_Labels> optional = cardLabelsRepository.findById(id);
        if (optional.isPresent()) {
            Card_Labels entity = optional.get();
            entity.setCardId(dto.getCardId());
            entity.setLabelId(dto.getLabelId());
            entity.setMemberId(dto.getMemberId());
            Card_Labels updated = cardLabelsRepository.save(entity);
            return toResponseDto(updated);
        }
        return null;
    }

    // Card label sil
    public boolean deleteCardLabel(Integer id) {
        if (cardLabelsRepository.existsById(id)) {
            cardLabelsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Entity'den Response DTO'ya dönüşüm
    private CardLabelsResponse toResponseDto(Card_Labels entity) {
        CardLabelsResponse dto = new CardLabelsResponse();
        dto.setCardLabelId(entity.getCardLabelId());
        dto.setCardId(entity.getCardId());
        dto.setLabelId(entity.getLabelId());
        dto.setMemberId(entity.getMemberId());
        return dto;
 }
}
