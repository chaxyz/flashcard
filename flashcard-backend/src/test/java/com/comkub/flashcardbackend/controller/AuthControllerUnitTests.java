package com.comkub.flashcardbackend.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comkub.flashcardbackend.config.JWTAuthFilter;
import com.comkub.flashcardbackend.dto.UserDTO;
import com.comkub.flashcardbackend.dto.jwt.JwtLogin;
import com.comkub.flashcardbackend.dto.jwt.JwtResponse;
import com.comkub.flashcardbackend.dto.jwt.JwtSignup;
import com.comkub.flashcardbackend.services.AuthService;
import com.comkub.flashcardbackend.services.JWTUtils;
import com.comkub.flashcardbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;
    private JwtSignup jwtSignup;
    private JwtLogin jwtLogin;
    private JwtResponse jwtResponse;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        jwtSignup = new JwtSignup();
        jwtSignup.setUsername("testuser");
        jwtSignup.setPassword("password");

        jwtLogin = new JwtLogin();
        jwtLogin.setUsername("testuser");
        jwtLogin.setPassword("password");

        userDTO = new UserDTO("testuser", "name");

        jwtResponse = new JwtResponse("dummyAccessToken", "dummyRefreshToken");
    }

    @Test
    void testSignUp() throws Exception {
        when(authService.signUp(any(JwtSignup.class))).thenReturn(userDTO);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtSignup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testSignIn() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authService.signIn(any(JwtLogin.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("dummyAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("dummyRefreshToken"));
    }

    @Test
    void testSignInInvalidCredentials() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtLogin)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshToken() throws Exception {
        String token = "Bearer dummyRefreshToken";
        String onlyToken = "dummyRefreshToken";

        when(jwtUtils.validateRefreshToken(onlyToken)).thenReturn(true);
        when(authService.refreshToken(any(String.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/token")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("dummyAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("dummyRefreshToken"));
    }

    @Test
    void testRefreshTokenInvalid() throws Exception {
        String token = "Bearer invalidRefreshToken";
        String onlyToken = "invalidRefreshToken";

        when(jwtUtils.validateRefreshToken(onlyToken)).thenReturn(false);

        mockMvc.perform(post("/token")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }
}