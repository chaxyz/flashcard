package com.comkub.flashcardbackend.dto.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtSignup {
    @Size(min = 8, max = 100)
    private String username;
    @Size(max = 50)
    private String name;
    @Size(max = 320)
    private String email;
    @Size(min = 8, max = 255)
    private String password;
    @Pattern(regexp = "USER|ADMIN", message = "Can not be that role")
    private String role = "USER";
}
