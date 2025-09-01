package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(TeacherControllerErrorsTest.TestErrorHandler.class) 
class TeacherControllerErrorsTest {

    @Autowired MockMvc mvc;

    @MockBean TeacherService teacherService;
    @MockBean JwtUtils jwtUtils;
    @MockBean UserDetailsServiceImpl userDetailsService;

    @RestControllerAdvice
    static class TestErrorHandler {
        @ExceptionHandler(RuntimeException.class)
        ResponseEntity<?> handle(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "unexpected"));
        }
    }

    @Test
    @DisplayName("GET /api/teacher/{id} -> 500 si le service lève une RuntimeException")
    void getById_unexpectedError_returns500() throws Exception {
        when(teacherService.findById(99L)).thenThrow(new RuntimeException("boom"));

        mvc.perform(get("/api/teacher/{id}", 99L))
           .andExpect(status().isInternalServerError());
    }
}
