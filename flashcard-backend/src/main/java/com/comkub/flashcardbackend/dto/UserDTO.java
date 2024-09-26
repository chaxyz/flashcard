package com.comkub.flashcardbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String role;

    public UserDTO(String username, String name) {
        this.username = username;
        this.name = name;
    }
}


