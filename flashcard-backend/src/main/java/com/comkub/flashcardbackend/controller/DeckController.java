package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.dto.DeckDTO;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.DeckOfUser;
import com.comkub.flashcardbackend.services.DeckOfUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private DeckOfUserService deckOfUserService;

    @GetMapping("/{id}")
    public ResponseEntity<Deck> getDecksById(@RequestHeader("Authorization") String token , @PathVariable Integer id) {
       return  ResponseEntity.ok(deckOfUserService.getDeckDetailById(token,id));
    }

    @GetMapping("")
    public ResponseEntity<List<DeckOfUser>> getAllDeckOfUser(@RequestHeader("Authorization") String token){
        return   ResponseEntity.ok(deckOfUserService.findAllDeckOfUser(token));
    }

    @PostMapping("")
    public ResponseEntity<DeckDTO> addNewDeck(@RequestHeader("Authorization") String token, @Valid  @RequestBody DeckDTO deck){
        return   ResponseEntity.ok(deckOfUserService.createNewDeck(deck,token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editDeck(@RequestHeader("Authorization") String token, @Valid  @RequestBody DeckDTO deck , @PathVariable Integer id){
        return   ResponseEntity.ok(deckOfUserService.editDeck(deck,token,id));
    }
}
