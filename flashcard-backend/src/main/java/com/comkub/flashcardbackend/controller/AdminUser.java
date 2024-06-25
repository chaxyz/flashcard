package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.repository.UserRepository;
import com.comkub.flashcardbackend.services.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUser {

    @Autowired
    private JWTUtils jwtUtils ;

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userOnly(@RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
            System.out.println("aac");
        }
        return  ResponseEntity.ok(jwtUtils.extractUsername(token));
    }
    @GetMapping("/adminuser/alone")
    public ResponseEntity<Object> both(){
        return  ResponseEntity.ok("both can  see ");
    }
}
