package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.services.DeckService;
import com.comkub.flashcardbackend.services.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private DeckService deckService;

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getDecksById(@RequestHeader("Authorization") String token , @PathVariable Integer id) {
//        String onlyToken = JWTUtils.getOnlyToken(token);
//
//    }
}
