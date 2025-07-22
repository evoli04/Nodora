package com.example.demo.model.cards;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "cards", schema = "public")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "title", nullable = false, length = 15)
    private String title;

    @Column(name = "description", nullable = false, length = 25)
    private String description;

    @Column(name = "ending_date")
    private Date endingDate;

    @Column(name = "list_id", nullable = false)
    private Integer listId;

    @Column(name = "position", nullable = false)
    private Integer position = 0;  // Varsayılan değer


    // Getters and Setters
    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    // Position için getter ve setter
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
}
}