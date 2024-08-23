package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.dto.DeckDTO;
import com.comkub.flashcardbackend.dto.UserDTO;
import com.comkub.flashcardbackend.services.JWTUtils;
import com.comkub.flashcardbackend.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private JWTUtils jwtUtils ;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserDetail(@RequestHeader("Authorization") String token){
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (jwtUtils.isTokenValid(token, userDetails)) {
           return ResponseEntity.ok(mapper.map(userService.loadUserByUsername(userDetails.getUsername()), UserDTO.class));
        } else {
            return ResponseEntity.status(401).body("Invalid Token");
        }
    }


}
