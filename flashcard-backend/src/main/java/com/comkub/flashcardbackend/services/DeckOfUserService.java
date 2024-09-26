package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.DeckDTO;
import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.DeckOfUser;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.exception.ItemNotFoundException;
import com.comkub.flashcardbackend.repository.DeckOfUserRepository;
import com.comkub.flashcardbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DeckOfUserService {

    @Autowired
    private DeckService deckService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckOfUserRepository deckOfUserRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ModelMapper modelMapper;

    public List<DeckOfUser>findAllDeckOfUser(String token){
        User user = getUserFromToken(token);
        return deckOfUserRepository.findAllByUser(user);
    }

    public  List<Card> getCardInDeck(String token,int deckId){
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null){
            return cardService.getAllCard(deckId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public DeckDTO createNewDeck(DeckDTO deckDTO, String token) {
        User user = getUserFromToken(token);
        try {
            Deck deck = deckService.addDeck(modelMapper.map(deckDTO, Deck.class));
            if(user != null && deck != null){
                DeckOfUser deckOfUser = new DeckOfUser( deck,user, DeckOfUser.Role.OWNER);
                deckOfUserRepository.save(deckOfUser);
            }
            DeckDTO newDeck = modelMapper.map(deck, DeckDTO.class);
            newDeck.setUser(user);
            return newDeck;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public DeckDTO editDeck(DeckDTO deckDTO, String token , int deckId) {
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null){
            Deck deck = deckService.updateDeck(deckId, modelMapper.map(deckDTO, Deck.class));
            return modelMapper.map(deck, DeckDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public DeckDTO deleteDeck(String token , int deckId) {
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null){
            Deck deck = deckService.deleteDeck(deckId);
            return modelMapper.map(deck, DeckDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public Deck getDeckDetailById(String token ,int deckId){
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null){
            return  deckService.getDeckById(deckId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }



    private DeckOfUser validateUserAndBoard(String token, int deckId) {
        User user = extractUserFromToken(token);
        Deck deck = deckService.getDeckById(deckId);
        return deckOfUserRepository.findDeckOfUserByDeckAndUser(deck,user);
    }

    private DeckOfUser getDeckOfUser(int boardId) {
        Deck deck = deckService.getDeckById(boardId);
        return deckOfUserRepository.findDeckOfUserByDeckAndUser_Role(deck, DeckOfUser.Role.OWNER);
    }


    private User getUserFromToken(String token) {
        User user = extractUserFromToken(token);
        Integer userId = user.getId();
        return userRepository.findById(userId).orElseThrow(() -> new ItemNotFoundException("User not found"));
    }

    private User extractUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtils.extractUsername(token);
        return userRepository.findByUsername(username).orElseThrow(() -> new ItemNotFoundException("User not found"));
    }
}
