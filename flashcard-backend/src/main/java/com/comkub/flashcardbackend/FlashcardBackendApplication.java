package com.comkub.flashcardbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.comkub.flashcardbackend"})
@EnableJpaRepositories(basePackages = "com.comkub.flashcardbackend.repository")
@EntityScan(basePackages = "com.comkub.flashcardbackend.entity")
@SpringBootApplication
public class FlashcardBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashcardBackendApplication.class, args);
    }

}
