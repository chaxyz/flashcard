package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.services.DeckOfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/deck/{id}/card")
public class CardController {

    @Autowired
    private DeckOfUserService deckOfUserService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDecksById(@RequestHeader("Authorization") String token , @PathVariable Integer id) {
        return  ResponseEntity.ok(deckOfUserService.getCardInDeck(token,id));
    }
}
