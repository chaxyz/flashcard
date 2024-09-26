package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.DeckOfUser;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.exception.ItemNotFoundException;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.repository.CardRepository;
import com.comkub.flashcardbackend.repository.DeckOfUserRepository;
import com.comkub.flashcardbackend.repository.DeckRepository;
import com.comkub.flashcardbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckOfUserRepository deckOfUserRepository;

    @Autowired
    private JWTUtils jwtUtils;
    public List<DeckOfUser>findAllDeckOfUser(String token){
        User user = getUserFromToken(token);
        return deckOfUserRepository.findAllByUser(user);
    }

    public  List<Card> getCardInDeck(int deckId){
        return cardRepository.findAllByDeck_Id(deckId);
    }

    public Deck getDeckDetailById(String token ,int deckId){
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null){
            return  getDeckById(deckId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    private DeckOfUser validateUserAndBoard(String token, int deckId) {
        User user = extractUserFromToken(token);
        Deck deck = getDeckById(deckId);
        return deckOfUserRepository.findDeckOfUserByDeckAndUser(deck,user);
    }

    private DeckOfUser getDeckOfUser(int boardId) {
        Deck deck = getDeckById(boardId);
        return deckOfUserRepository.findDeckOfUserByDeckAndUser_Role(deck, DeckOfUser.Role.OWNER);
    }

    private User getUserFromToken(String token) {
        User user = extractUserFromToken(token);
        Integer userId = user.getId();
        return userRepository.findById(userId).orElseThrow(() -> new ItemNotFoundException("User not found"));
    }

    public Deck getDeckById(int deckId) {
        return deckRepository.findById(deckId).orElseThrow(() -> new ItemNotFoundException("Deck id '" + deckId + "' not found"));
    }

    private User extractUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtils.extractUsername(token);
        return userRepository.findByUsername(username).orElseThrow(() -> new ItemNotFoundException("User not found"));
    }
}
