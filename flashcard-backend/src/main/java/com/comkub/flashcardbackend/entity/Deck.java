package com.comkub.flashcardbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(max = 100)
    private String deckTitle;

    @Size(max = 500)
    private String deckDescription;

    @Column(name = "createdOn" , insertable = false , updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn", insertable = false , updatable = false)
    private ZonedDateTime updatedOn;

    private boolean isPublic;
}
