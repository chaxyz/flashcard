package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.CardDTO;
import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCard(int id){
        return  cardRepository.findAllByDeck_Id(id);
    }

    public Card createNewCard(Card card){
        return  cardRepository.save(card);
    }

    public Card getCardById(int id){
        return cardRepository.findById(id).orElseThrow(() -> new NotFoundException("Card id " + id + " found"));
    }

    public Card editCard(int cardId,CardDTO cardDTO){
        Card card = getCardById(cardId);
        card.setCardDescription(cardDTO.getCardDescription());
        card.setCardTitle(cardDTO.getCardTitle());
        card.setCardMeaning(cardDTO.getCardMeaning());
        return  card;
    }

    public void deleteCard(int cardId){
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }

}
