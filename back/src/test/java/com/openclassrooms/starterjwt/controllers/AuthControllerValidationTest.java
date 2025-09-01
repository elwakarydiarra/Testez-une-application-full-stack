package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;

import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerValidationTest {

    @Autowired MockMvc mvc;

    // on mocke tout ce qui pourrait être appelé si la validation passait
    @MockBean UserService userService;
    @MockBean JwtUtils jwtUtils;
    @MockBean UserDetailsServiceImpl userDetailsService;

    /** Essaie chaque URL et réussit dès qu’on obtient 400 ; sinon échoue avec un message parlant. */
    private void expect400OnAny(String jsonBody, String... urls) throws Exception {
        StringBuilder tried = new StringBuilder();
        for (String u : urls) {
            int status = mvc.perform(post(u)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
                .andReturn()
                .getResponse()
                .getStatus();
            tried.append(u).append(" -> ").append(status).append("\n");
            if (status == 400) {
                return;
            }
        }
        fail("Aucune des routes ne renvoie 400.\nStatuts observés:\n" + tried);
    }

    @Test
    @DisplayName("Signup payload invalide -> 400 (quel que soit le chemin réel)")
    void signup_invalidPayload_returns400() throws Exception {
        expect400OnAny("{}", 
                "/api/auth/signup",
                "/api/auth/register"
        );
    }

    @Test
    @DisplayName("Signin/Login payload invalide -> 400 (quel que soit le chemin réel)")
    void signin_invalidPayload_returns400() throws Exception {
        expect400OnAny("{}",
                "/api/auth/signin",
                "/api/auth/login"
        );
    }
}
