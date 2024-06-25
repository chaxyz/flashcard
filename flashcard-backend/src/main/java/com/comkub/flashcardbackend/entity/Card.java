package com.comkub.flashcardbackend.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cardTitle;
    private String cardDescription;
    private String cardMeaning;
    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;
}
