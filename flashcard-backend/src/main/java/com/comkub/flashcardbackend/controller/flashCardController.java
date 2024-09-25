//package com.comkub.flashcardbackend.controller;
//
//import com.comkub.flashcardbackend.dto.CardDTO;
//import com.comkub.flashcardbackend.dto.DeckDTO;
//import com.comkub.flashcardbackend.entity.Deck;
//import com.comkub.flashcardbackend.entity.User;
//import com.comkub.flashcardbackend.services.DeckService;
//import com.comkub.flashcardbackend.services.JWTUtils;
//import com.comkub.flashcardbackend.services.ListMapper;
//import com.comkub.flashcardbackend.services.UserService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/flashcard")
//public class flashCardController {
//    @Autowired
//    private DeckService deckService;
//
//    @Autowired
//    private JWTUtils jwtUtils ;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ListMapper listMapper;
//
//    @Autowired
//    private ModelMapper mapper;
//
//    @GetMapping("/decks")
//    public ResponseEntity<?> getAllDecks(@RequestHeader("Authorization") String token) {
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        String username = jwtUtils.extractUsername(token);
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        if (jwtUtils.isTokenValid(token, userDetails)) {
//           List<DeckDTO> decks = listMapper.mapList(deckService.findAllDeckOfUser(userDetails),DeckDTO.class,mapper);
//            return ResponseEntity.ok(decks);
//        } else {
//            return ResponseEntity.status(401).body("Invalid Token");
//        }
//    }
//
//    @GetMapping("/decks/{id}")
//    public ResponseEntity<?> getDecksById(@RequestHeader("Authorization") String token , @PathVariable Integer id) {
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        String username = jwtUtils.extractUsername(token);
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        if (jwtUtils.isTokenValid(token, userDetails) || deckService.getDeckDetailById(id).isPublic()) {
//            List<CardDTO> cards = listMapper.mapList(deckService.getCardInDeck(id), CardDTO.class, mapper);
//            return ResponseEntity.ok(cards);
//        } else {
//            return ResponseEntity.status(401).body("Invalid Token");
//        }
//    }
//    @PostMapping("/deck/add")
//    public void addNewDeck(@RequestHeader("Authorization") String token, @RequestBody Deck deck){
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        String username = jwtUtils.extractUsername(token);
//        User userDetails = (User)userService.loadUserByUsername(username);
//        if (jwtUtils.isTokenValid(token, userDetails) ) {
////           deckService.addNewDeck(deck.getCards(),userDetails);
//        } else {
//
//        }
//    }
//
//}
