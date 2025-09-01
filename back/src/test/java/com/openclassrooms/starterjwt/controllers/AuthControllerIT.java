package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc             
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired private MockMvc mvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper om = new ObjectMapper();

    private String SIGNUP_PATH;
    private String SIGNIN_PATH;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();
        SIGNUP_PATH = resolveFirstExisting(
                "/api/auth/signup",
                "/api/auth/register"
        );
        SIGNIN_PATH = resolveFirstExisting(
                "/api/auth/signin",
                "/api/auth/login",
                "/api/auth/authenticate"
        );
        assertNotNull(SIGNUP_PATH, "Impossible de trouver l'endpoint d'inscription");
        assertNotNull(SIGNIN_PATH, "Impossible de trouver l'endpoint de login");
    }

    /** Retourne le 1er endpoint candidat qui ne renvoie pas 404 en POST JSON. */
    private String resolveFirstExisting(String... candidates) throws Exception {
        for (String c : candidates) {
            MvcResult r = mvc.perform(post(c)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andReturn();
            if (r.getResponse().getStatus() != 404) return c;
        }
        return null;
    }

    private String signupJson(String email, String pwd, String first, String last) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", pwd);
        body.put("firstName", first);
        body.put("lastName", last);
        return om.writeValueAsString(body);
    }

    private String loginJson(String email, String pwd) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", pwd);
        return om.writeValueAsString(body);
    }

    @Test
    @DisplayName("Signup OK puis Signin OK (endpoints autodétectés)")
    void signup_ok_then_login_ok() throws Exception {
        mvc.perform(post(SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson("alice@test.com", "pwd12345", "Alice", "Doe")))
           .andExpect(status().isOk());

        mvc.perform(post(SIGNIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("alice@test.com", "pwd12345")))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.token", notNullValue()))
           .andExpect(jsonPath("$.username").value("alice@test.com"));
    }

    @Test
    @DisplayName("Signup -> 400 quand email déjà utilisé (endpoints autodétectés)")
    void signup_emailAlreadyUsed_returns400() throws Exception {
        userRepository.save(User.builder()
                .email("used@test.com")
                .password(passwordEncoder.encode("whatever"))
                .firstName("U")
                .lastName("Sed")
                .admin(false)
                .build());

        mvc.perform(post(SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson("used@test.com", "pwd", "U", "Sed")))
           .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Signin -> 401 mauvaises crédentials (endpoints autodétectés)")
    void login_badCredentials_returns401() throws Exception {
        userRepository.save(User.builder()
                .email("bob@test.com")
                .password(passwordEncoder.encode("right-password"))
                .firstName("Bob")
                .lastName("Doe")
                .admin(false)
                .build());

        mvc.perform(post(SIGNIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("bob@test.com", "wrong-password")))
           .andExpect(status().isUnauthorized());
    }
}
