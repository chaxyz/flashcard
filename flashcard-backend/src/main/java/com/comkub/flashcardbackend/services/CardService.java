package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.entity.Card;
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

}
