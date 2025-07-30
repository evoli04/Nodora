package com.example.demo.service;

import com.example.demo.model.cards.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {
    Card createCard(Card card);
    Optional<Card> getCardById(Integer id);
    List<Card> getAllCards();
    Card updateCard(Integer id, Card updatedCard);
    void deleteCard(Integer id);
}