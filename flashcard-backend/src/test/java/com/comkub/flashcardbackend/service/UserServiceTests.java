package com.comkub.flashcardbackend.service;

import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.repository.UserRepository;
import com.comkub.flashcardbackend.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;


    @BeforeEach
    public void setup(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user = User.builder()
                .id(1)
                .username("exampleUser1")
                .name("John Doe")
                .email("sam@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(User.SystemRole.USER)
                .build();

    }

    @Test
    @Order(1)
    public void saveEmployeeTest(){
        // precondition
        given(userRepository.save(user)).willReturn(user);

        //action
        User savedUser = userService.addUser(user);

        // verify the output
        System.out.println(savedUser);
        assertThat(savedUser).isNotNull();
    }

    @Test
    @Order(2)
    public void getEmployeeById(){
        // precondition
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // action
        System.out.println(userRepository.findById(1));
        User existingUser = userService.findUserByUsername(user.getUsername());

        // verify
        System.out.println(existingUser);
        assertThat(existingUser).isNotNull();

    }





}


