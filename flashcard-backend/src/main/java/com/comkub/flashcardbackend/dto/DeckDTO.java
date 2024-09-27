package com.comkub.flashcardbackend.dto;


import com.comkub.flashcardbackend.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DeckDTO {
    private String deckTitle;
    private String deckDescription;
    private Boolean publicVisibility;
    private UserDTO owner;

    public void setUser(User user) {
        if(owner == null) {
            return;
        }
        this.owner = new UserDTO(user.getUsername(), user.getName());
    }

}
