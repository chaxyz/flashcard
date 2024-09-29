package com.comkub.flashcardbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "deck_of_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckOfUser {
    @EmbeddedId
    private DeckUserKey id;
    @Enumerated(EnumType.STRING)

    @Column(name = "role", nullable = false)
    private  Role role;

    @ManyToOne
    @MapsId("deckId")
    @JoinColumn(name = "decks_id")
    Deck deck;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "users_id")
    User user;

    public DeckOfUser(Deck deck, User user, Role role) {
       this.id = new DeckUserKey();
       this.id.deckId = deck.getId();
       this.id.userId = user.getId();
       this.deck = deck;
       this.user = user;
       this.role = role;
    }

    @Embeddable
    @Data
    public static class DeckUserKey implements Serializable {

        @Column(name = "users_id")
        int userId;

        @Column(name = "decks_id")
        int deckId;

    }

    public enum Role {
        OWNER,
        VISITOR,
        EDITOR,
    }
}
