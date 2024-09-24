package com.comkub.flashcardbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FlashcardBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashcardBackendApplication.class, args);
    }

}
