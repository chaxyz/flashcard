package com.comkub.flashcardbackend.repository;

import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck,Integer> {
    List<Deck> findAllByUser(User user);
}
