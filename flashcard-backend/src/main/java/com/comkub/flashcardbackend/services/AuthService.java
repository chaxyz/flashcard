package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.RequestResponse;
import com.comkub.flashcardbackend.dto.UserDTO;
import com.comkub.flashcardbackend.dto.jwt.JwtLogin;
import com.comkub.flashcardbackend.dto.jwt.JwtResponse;
import com.comkub.flashcardbackend.dto.jwt.JwtSignup;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Service
public class AuthService {
    private UserService userService;
    private JWTUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    public AuthService(JWTUtils jwtUtils, PasswordEncoder passwordEncoder, UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserDTO signUp(JwtSignup register){
        try {
            if(userService.validateUser(register)) {
                User user = new User();
                user.setUsername(register.getUsername());
                user.setPassword(passwordEncoder.encode(register.getPassword()));
                user.setEmail(register.getEmail());
                user.setRole(User.SystemRole.USER);
                user.setName(register.getName());
                User userResult = userService.addUser(user);
                return modelMapper.map(userResult, UserDTO.class);
            }
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Signup failed");
       }catch (Exception e){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
       }
    }

   public JwtResponse signIn(JwtLogin signIn){
           User user = userService.findUserByUsername(signIn.getUsername());
           String jwt = jwtUtils.generateToken(user);
           String refreshToken = jwtUtils.generateRefreshToken(user);
           return new JwtResponse(jwt, refreshToken);

   }
    public  JwtResponse refreshToken(String token){
       String username = jwtUtils.extractUsername(token);
        User user = userService.findUserByUsername(username);
       if(jwtUtils.validateRefreshToken(token)){
          String jwt = jwtUtils.generateToken(user);
          return new JwtResponse(jwt);
       }
       throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh-token");
    }

}
