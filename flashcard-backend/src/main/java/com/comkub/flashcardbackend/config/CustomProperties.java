package com.comkub.flashcardbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties("custom")
public class CustomProperties {
    private String jwtSecret;

    private String jwtLifetime;

}
