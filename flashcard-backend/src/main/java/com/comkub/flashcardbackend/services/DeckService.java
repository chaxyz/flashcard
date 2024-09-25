package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.repository.CardRepository;
import com.comkub.flashcardbackend.repository.DeckRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class DeckService {
//
//    @Autowired
//    private DeckRepository deckRepository;
//
//    @Autowired
//    private CardRepository cardRepository;
//    public List<Deck>findAllDeckOfUser(UserDetails userDetails){
//        User user = (User) userDetails;
//        return deckRepository.findAllByUser(user);
//    }
//
//    public  List<Card> getCardInDeck(int deckId){
//        return cardRepository.findAllByDeck_Id(deckId);
//    }
//
//    public Deck getDeckDetailById(int deckId){
//        return  deckRepository.findById(deckId).orElseThrow(() -> new RuntimeException("NOT FOUND"));
//    }
//
//
//}
