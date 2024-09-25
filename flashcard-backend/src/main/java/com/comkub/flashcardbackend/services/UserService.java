package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.jwt.JwtSignup;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    protected User addUser(User user) {
        return userRepository.save(user);
    }

    protected User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->new NotFoundException("User not found"));
    }
    protected boolean validateUser(JwtSignup register) {
        if(userRepository.existsByUsername(register.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        if(userRepository.existsByEmail(register.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email is already in use");
        }
        return true;
    }
}
