package com.comkub.flashcardbackend.dto;

import com.comkub.flashcardbackend.entity.Deck;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CardDTO {
    private Integer id;
    private String cardTitle;
    private String cardDescription;
    private String cardMeaning;
    private int deck;

    public void setDeck(Deck deck) {
        this.deck = deck.getId();
    }
}
