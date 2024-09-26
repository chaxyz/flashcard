package com.comkub.flashcardbackend.repository;

import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck,Integer> {
}
