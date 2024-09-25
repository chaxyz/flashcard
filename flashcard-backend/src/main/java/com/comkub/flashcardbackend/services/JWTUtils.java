package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.config.CustomProperties;
import com.comkub.flashcardbackend.entity.User;
import com.comkub.flashcardbackend.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    private final SecretKey Key;
    private final long EXPIRATION_TIME;
    private final long REFRESH_TIME;
    private final UserRepository userRepository;

    @Autowired
    public JWTUtils(CustomProperties  customProperties , UserRepository userRepository) {
        String secreteString = customProperties.getJwtSecret();
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.EXPIRATION_TIME = Long.parseLong(customProperties.getJwtLifetime());
        this.REFRESH_TIME = Long.parseLong(customProperties.getJwtRefreshLifetime());
        this.userRepository = userRepository;
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("id", user.getId());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        return doGenerateToken(claims, user);
    }

    private String doGenerateToken(Map<String, Object> claims, User userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String generateRefreshToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction){
        return claimsFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        Jwts.parser().verifyWith(Key).build().parse(token);
        return  userDetails.getUsername().equals(extractUsername(token));
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public Boolean validateRefreshToken(String token) {
        final String username = extractUsername(token);
        return userRepository.existsByUsername(username) && !isTokenExpired(token);
    }

    public static  String getOnlyToken(String token){
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
