package com.example.demo.service;

import com.example.demo.model.cards.Card;
import com.example.demo.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Optional<Card> getCardById(Integer id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card updateCard(Integer id, Card updatedCard) {
        return cardRepository.findById(id).map(card -> {
            card.setTitle(updatedCard.getTitle());
            card.setDescription(updatedCard.getDescription());
            card.setEndingDate(updatedCard.getEndingDate());
            card.setListId(updatedCard.getListId());
            card.setPosition(updatedCard.getPosition());
            card.setMemberId(updatedCard.getMemberId());
            return cardRepository.save(card);
        }).orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Override
    public void deleteCard(Integer id) {
        cardRepository.deleteById(id);
  }
}