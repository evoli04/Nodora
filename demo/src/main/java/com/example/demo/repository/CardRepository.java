package com.example.demo.repository;

import com.example.demo.model.cards.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByListId(Integer listId);
    List<Card> findByMemberId(Integer memberId);
}