package com.comkub.flashcardbackend.dto;

import com.comkub.flashcardbackend.entity.Deck;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardDTO {
    private Integer id;
    @Size(max=100)
    private String cardTitle;
    @Size(max=500)
    private String cardDescription;
    @Size(max=500)
    private String cardMeaning;

}