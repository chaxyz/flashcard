package com.comkub.flashcardbackend.repository;

import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.DeckOfUser;
import com.comkub.flashcardbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckOfUserRepository extends JpaRepository<DeckOfUser , DeckOfUser.DeckUserKey> {
    DeckOfUser findDeckOfUserByDeckAndUser(Deck deck , User user);

    DeckOfUser findDeckOfUserByDeckAndUser_Role(Deck deck, DeckOfUser.Role role);

    List<DeckOfUser> findAllByUser(User user);
}


