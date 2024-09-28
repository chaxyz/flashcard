package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.CardDTO;
import com.comkub.flashcardbackend.dto.DeckDTO;
import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.DeckOfUser;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.exception.NotFoundException;
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

    @Autowired
    private ListMapper listMapper;

    public List<DeckOfUser>findAllDeckOfUser(String token){
        User user = getUserFromToken(token);
        return deckOfUserRepository.findAllByUser(user);
    }

    public  List<CardDTO> getCardInDeck(String token,int deckId){
        Deck deck = deckService.getDeckById(deckId);
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(isPublicAccessibility(deck) || (deckOfUser != null && canAccess(deckOfUser))){
            return listMapper.mapList(cardService.getAllCard(deckId),CardDTO.class,modelMapper);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public  Card getCardInDeckById(String token,int deckId, int cardId){
        Deck deck = deckService.getDeckById(deckId);
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(isPublicAccessibility(deck) || (deckOfUser != null && canAccess(deckOfUser))){
            return cardService.getCardById(cardId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public CardDTO creatNewCardInDeck(String token,int deckId, CardDTO cardDTO){
        Deck deck = deckService.getDeckById(deckId);
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null && canModify(deckOfUser)){
            Card card = modelMapper.map(cardDTO,Card.class);
            card.setDeck(deck);
            return modelMapper.map(cardService.createNewCard(card), CardDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public Card editCard(CardDTO cardDTOO, String token , int deckId , int cardId) {
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null && canModify(deckOfUser)){
            return cardService.editCard(cardId,cardDTOO);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public void deleteCard(String token , int deckId , int cardId) {
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null && canModify(deckOfUser)){
            cardService.deleteCard(cardId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public DeckDTO createNewDeck(DeckDTO deckDTO, String token) {
        User user = getUserFromToken(token);
        try {
            Deck oldDeck = modelMapper.map(deckDTO, Deck.class);
            Deck deck = deckService.addDeck(oldDeck);
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
        if(deckOfUser != null && isOwner(deckOfUser)){
            Deck deck = deckService.updateDeck(deckId, modelMapper.map(deckDTO, Deck.class));
            return modelMapper.map(deck, DeckDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public DeckDTO deleteDeck(String token , int deckId) {
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(deckOfUser != null && isOwner(deckOfUser)){
            Deck deck = deckService.deleteDeck(deckId);
            return modelMapper.map(deck, DeckDTO.class);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }

    public DeckDTO getDeckDetailById(String token ,int deckId){
        Deck deck = deckService.getDeckById(deckId);
        DeckOfUser deckOfUser = validateUserAndBoard(token,deckId);
        if(isPublicAccessibility(deck) || (deckOfUser != null && canAccess(deckOfUser))){
            DeckDTO deckDTO = modelMapper.map(deck, DeckDTO.class);
            deckDTO.setUser(getDeckOfUser(deck).getUser());
            return  deckDTO;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not a deck owner");
    }



    private DeckOfUser validateUserAndBoard(String token, int deckId) {
        User user = extractUserFromToken(token);
        Deck deck = deckService.getDeckById(deckId);
        return deckOfUserRepository.findDeckOfUserByDeckAndUser(deck,user);
    }

    private DeckOfUser getDeckOfUser(Deck deck) {
        return deckOfUserRepository.findDeckOfUserByDeckAndUser_Role(deck, DeckOfUser.Role.OWNER);
    }


    private User getUserFromToken(String token) {
        User user = extractUserFromToken(token);
        Integer userId = user.getId();
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private User extractUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtils.extractUsername(token);
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean isPublicAccessibility(Deck deck) {
        return deck.getPublicVisibility();
    }

    private boolean canModify(DeckOfUser deckOfUser) {
        return deckOfUser.getRole().toString().equals("OWNER") || deckOfUser.getRole().toString().equals("EDITOR");
    }

    private boolean canAccess(DeckOfUser deckOfUser) {
        return deckOfUser.getRole().toString().equals("OWNER") || deckOfUser.getRole().toString().equals("EDITOR") || deckOfUser.getRole().toString().equals("VISITOR");
    }

    private boolean isOwner(DeckOfUser deckOfUser) {
        return deckOfUser.getRole().toString().equals("OWNER");
    }
}
