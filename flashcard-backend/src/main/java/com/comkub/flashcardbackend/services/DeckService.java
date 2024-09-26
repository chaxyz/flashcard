package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.exception.ItemNotFoundException;
import com.comkub.flashcardbackend.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    protected Deck getDeckById(int deckId) {
        return deckRepository.findById(deckId).orElseThrow(() -> new ItemNotFoundException("Deck id '" + deckId + "' not found"));
    }

    protected  Deck addDeck(Deck deck) {
        return deckRepository.save(deck);
    }

    protected Deck updateDeck(int deckId, Deck newDeck) {
        Deck deck = getDeckById(deckId);
        deck.setDeckTitle(newDeck.getDeckTitle());
        deck.setDeckDescription(newDeck.getDeckDescription());
        deck.setPublic(newDeck.isPublic());
        return deckRepository.save(deck);
    }
    protected Deck deleteDeck(int deckId) {
        Deck deck = getDeckById(deckId);
        deckRepository.delete(deck);
        return deck;
    }
}
