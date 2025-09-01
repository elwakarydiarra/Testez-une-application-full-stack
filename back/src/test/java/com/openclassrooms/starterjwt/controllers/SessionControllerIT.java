package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SessionControllerIT {

    @Autowired private MockMvc mvc;
    @Autowired private SessionRepository sessionRepository;

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/session/{id} renvoie 200 et la session persistée")
    void getById_returnsPersistedSession() throws Exception {
        Session s = sessionRepository.save(Session.builder()
                .name("Test Yoga")
                .date(new Date())
                .description("desc")
                .build());

        mvc.perform(get("/api/session/{id}", s.getId())
                .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith("application/json"))
           .andExpect(jsonPath("$.id").value(s.getId()))
           .andExpect(jsonPath("$.name").value("Test Yoga"));

        assertThat(sessionRepository.findById(s.getId())).isPresent();
    }
}
