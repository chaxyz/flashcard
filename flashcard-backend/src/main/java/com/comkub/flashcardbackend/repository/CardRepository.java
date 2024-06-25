package com.comkub.flashcardbackend.repository;

import com.comkub.flashcardbackend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Integer> {
}
