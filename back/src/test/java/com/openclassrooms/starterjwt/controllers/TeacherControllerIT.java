package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired TeacherRepository teacherRepository;

    private Teacher t1;

    @BeforeEach
    void setup() {
        teacherRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();
        t1 = teacherRepository.save(Teacher.builder()
                .firstName("John").lastName("Doe")
                .createdAt(now).updatedAt(now).build());
        teacherRepository.save(Teacher.builder()
                .firstName("Jane").lastName("Roe")
                .createdAt(now).updatedAt(now).build());
    }

    @Test
    @WithMockUser
    void findAll_returnsOk_andList() throws Exception {
        mvc.perform(get("/api/teacher").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].firstName", not(isEmptyOrNullString())));
    }

    @Test
    @WithMockUser
    void getById_returnsOk_andItem() throws Exception {
        mvc.perform(get("/api/teacher/{id}", t1.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(t1.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
