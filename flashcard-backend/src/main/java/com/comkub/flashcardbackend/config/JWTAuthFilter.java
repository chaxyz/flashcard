package com.comkub.flashcardbackend.config;

import com.comkub.flashcardbackend.services.JWTUtils;
import com.comkub.flashcardbackend.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        try {
            String jwt = parseJwt(requestTokenHeader);
            if (jwt == null || jwt.isBlank()) {
                System.out.println("yes");
                chain.doFilter(request, response);
                return;
            }
            String username = jwtUtils.extractUsername(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SecurityException | MalformedJwtException e) {
            throw new AuthenticationException("Invalid JWT token") {
            };
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("Expired JWT token") {
            };
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationException("Unsupported JWT token") {
            };
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("Missing JWT token") {
            };
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failure") {
            };
        }
        chain.doFilter(request, response);
    }

    private String parseJwt(String request) {
        String token = null;
        if (request != null && request.startsWith("Bearer ")) {
            token = request.substring(7);
        }
        return token;
    }
}
