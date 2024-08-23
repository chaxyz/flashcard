package com.comkub.flashcardbackend.dto;


import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckDTO {
    private String deckTitle;
    private String deckDescription;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private boolean isPublic;
    private UserDTO user;

   public void  setUser(User user){
       String username = user.getUsername();
       this.user = new UserDTO();
       this.user.setEmail(username);
   }

}
