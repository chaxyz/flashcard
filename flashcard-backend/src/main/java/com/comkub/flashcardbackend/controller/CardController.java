package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.dto.CardDTO;
import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.services.DeckOfUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/deck/{id}/card")
public class CardController {

    @Autowired
    private DeckOfUserService deckOfUserService;

    @GetMapping("")
    public ResponseEntity<List<CardDTO>> getAllCardOfDeck(@RequestHeader("Authorization") String token , @PathVariable Integer id) {
        return  ResponseEntity.ok(deckOfUserService.getCardInDeck(token,id));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCardOfDeckById(@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable Integer cardId){
        return   ResponseEntity.ok(deckOfUserService.getCardInDeckById(token,id,cardId));
    }

    @PostMapping("")
    public ResponseEntity<CardDTO> addNewCardToDeck(@RequestHeader("Authorization") String token, @Valid @RequestBody CardDTO cardDTO , @PathVariable Integer id){
      return   ResponseEntity.ok(deckOfUserService.creatNewCardInDeck(token,id,cardDTO));
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<Card> editCardOfDeck(@RequestHeader("Authorization") String token, @Valid  @RequestBody CardDTO cardDTO , @PathVariable Integer id, @PathVariable Integer cardId){
        return   ResponseEntity.ok(deckOfUserService.editCard(cardDTO,token,id,cardId));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Objects> deleteCardInDeck (@RequestHeader("Authorization") String token, @PathVariable Integer id, @PathVariable Integer cardId){
        deckOfUserService.deleteCard(token,id,cardId);
        return   ResponseEntity.ok(null);
    }
}

