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

    private UserRepository userRepository;
    private JWTUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private ModelMapper modelMapper;

    @Autowired
    public AuthService(UserRepository userRepository, JWTUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = new ModelMapper();
    }

    public UserDTO signUp(JwtSignup register){
        if(userRepository.existsByUsername(register.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        if(userRepository.existsByEmail(register.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        try {
           User user = new User();
           user.setUsername(register.getUsername());
           user.setPassword(passwordEncoder.encode(register.getPassword()));
           user.setEmail(register.getEmail());
           user.setRole(User.SystemRole.USER);
           user.setName(register.getName());
           User userResult = userRepository.save(user);
           return modelMapper.map(userResult, UserDTO.class);
       }catch (Exception e){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Signup failed");
       }
    }

   public JwtResponse signIn(JwtLogin signIn){
           User user = userRepository.findByUsername(signIn.getUsername()).orElseThrow(() ->new NotFoundException("User not found"));
           String jwt = jwtUtils.generateToken(user);
           String refreshToken = jwtUtils.generateRefreshToken(user);
           return new JwtResponse(jwt, refreshToken);

   }
    public  JwtResponse refreshToken(String token){
        System.out.println(token);
       String username = jwtUtils.extractUsername(token);
       User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
       if(jwtUtils.validateRefreshToken(token)){
          String jwt = jwtUtils.generateToken(user);
          return new JwtResponse(jwt);
       }
       throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh-token");
    }

}
