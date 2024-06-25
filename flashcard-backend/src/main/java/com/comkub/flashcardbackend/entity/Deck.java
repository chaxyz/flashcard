package com.comkub.flashcardbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String deckTitle;
    private String deckDescription;
    @Column(name = "createdOn" , insertable = false , updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn", insertable = false , updatable = false)
    private ZonedDateTime updatedOn;
    private boolean isPublic;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
