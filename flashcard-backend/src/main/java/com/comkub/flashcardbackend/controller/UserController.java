package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.dto.UserDTO;
import com.comkub.flashcardbackend.entity.User;
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
        String username = jwtUtils.extractUsername(JWTUtils.getOnlyToken(token));
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }


}
