package com.comkub.flashcardbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;
}
