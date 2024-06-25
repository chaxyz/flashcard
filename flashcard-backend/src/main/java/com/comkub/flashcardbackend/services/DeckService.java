package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {
    // cloning

    @Autowired
    private DeckRepository repository;

    public List<Deck>findAllDeckOfUser(){
        return null;
    }

}
