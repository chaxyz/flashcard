package com.comkub.flashcardbackend.repository;

import com.comkub.flashcardbackend.entity.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryUnitTests {

    @Autowired
    private UserRepository userRepository;

    User user;
    @Test
    @DisplayName("Test 1:Save User Test")
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest(){
        //prepare
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user = User.builder()
                .username("exampleUser1")
                .name("John Doe")
                .email("sam@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(User.SystemRole.USER)
                .build();

        //action
        userRepository.save(user);

        //Verify
        System.out.println(user);
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    @DisplayName("Test 1:Get User Test")
    public void getEmployeeTest(){

        //Action
        User user = userRepository.findById(1).orElseThrow(() -> new RuntimeException("User Not Found"));;
        //Verify
        System.out.println(user);
        Assertions.assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    @DisplayName("Test 1:Get All User Test")
    public void getListOfEmployeesTest(){
        //Action
        List<User> users = userRepository.findAll();
        //Verify
        System.out.println(users);
        Assertions.assertThat(users.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("Test 1:Update User Test")
    public void updateEmployeeTest(){
        //Action
        User user = userRepository.findById(1).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setEmail("samcurran@gmail.com");
        User userUpdated =  userRepository.save(user);

        //Verify
        System.out.println(userUpdated);
        Assertions.assertThat(userUpdated.getEmail()).isEqualTo("samcurran@gmail.com");

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("Test 1:Delete User Test")
    public void deleteEmployeeTest(){
        //Action
        userRepository.deleteById(1);
        Optional<User> employeeOptional = userRepository.findById(1);

        //Verify
        Assertions.assertThat(employeeOptional).isEmpty();
    }

}

