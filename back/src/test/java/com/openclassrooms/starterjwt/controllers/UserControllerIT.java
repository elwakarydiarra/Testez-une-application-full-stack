package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired 
    MockMvc mvc;

    @Autowired 
    UserRepository userRepository;

    private User u;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();

        String email = "alice@example.com"; 

        u = userRepository.save(User.builder()
                .firstName("Alice")
                .lastName("Wonder")
                .email(email)
                .password("pwd123")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    @Test
    @WithMockUser(username = "alice@example.com")
    void getById_returnsOk_andUser() throws Exception {
        mvc.perform(get("/api/user/{id}", u.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(u.getId()))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Wonder"))
                .andExpect(jsonPath("$.email").value(u.getEmail()));
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = "ADMIN") 
    void deleteById_returnsOk_andActuallyDeletes() throws Exception {
        mvc.perform(delete("/api/user/{id}", u.getId()))
                .andExpect(status().isOk());
    }
}
