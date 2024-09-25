package com.comkub.flashcardbackend.controller;

import com.comkub.flashcardbackend.dto.RequestResponse;
import com.comkub.flashcardbackend.dto.UserDTO;
import com.comkub.flashcardbackend.dto.jwt.JwtLogin;
import com.comkub.flashcardbackend.dto.jwt.JwtResponse;
import com.comkub.flashcardbackend.dto.jwt.JwtSignup;
import com.comkub.flashcardbackend.services.AuthService;
import com.comkub.flashcardbackend.services.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils  jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid JwtSignup signup){
        return  ResponseEntity.ok(authService.signUp(signup));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody JwtLogin login){;
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user or password");
        }
        return  ResponseEntity.ok(authService.signIn(login));
    }
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestHeader("Authorization") String token){
        String onlyToken = null;
        if (token.startsWith("Bearer ")) {
            onlyToken = token.substring(7);
        }
        if (!jwtUtils.validateRefreshToken(onlyToken) || onlyToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh-token");
        }
        return  ResponseEntity.ok(authService.refreshToken(onlyToken));
    }

}
