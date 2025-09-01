package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // désactive les filtres Spring Security
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private JwtUtils jwtUtils;
    @MockBean private UserRepository userRepository;
    @MockBean private PasswordEncoder encoder;
    @MockBean private com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsServiceImpl;
    @MockBean private AuthEntryPointJwt unauthorizedHandler;

    // Neutraliser complètement JPA
    @MockBean(name = "jpaAuditingHandler")
    private Object jpaAuditingHandler;
    @MockBean(name = "jpaMappingContext")
    private Object jpaMappingContext;

    @Test
    void login_returnsOk() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("john.doe@mail.com");
        req.setPassword("secret");

        UserDetailsImpl principal = UserDetailsImpl.builder()
                .id(42L)
                .username("john.doe@mail.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("$2a$hash")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        Mockito.when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("fake.jwt.token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void register_returnsOk() throws Exception {
        SignupRequest req = new SignupRequest();
        req.setEmail("new.user@mail.com");
        req.setFirstName("New");
        req.setLastName("User");
        req.setPassword("password123");

        Mockito.when(userRepository.existsByEmail("new.user@mail.com")).thenReturn(false);
        Mockito.when(encoder.encode("password123")).thenReturn("$2a$encoded");

        User saved = User.builder()
                .id(100L)
                .email("new.user@mail.com")
                .firstName("New")
                .lastName("User")
                .password("$2a$encoded")
                .admin(false)
                .build();

        Mockito.when(userRepository.save(any(User.class))).thenReturn(saved);
        Mockito.when(userRepository.findByEmail("new.user@mail.com")).thenReturn(Optional.of(saved));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
