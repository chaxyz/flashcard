package com.comkub.flashcardbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "custom")
@Component
public class CustomProperties {
    private String jwtSecret;

    private String jwtLifetime;

    private String jwtRefreshLifetime;

}
