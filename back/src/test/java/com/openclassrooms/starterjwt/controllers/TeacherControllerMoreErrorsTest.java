package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@Import(TeacherControllerMoreErrorsTest.TestExceptionHandler.class)
class TeacherControllerMoreErrorsTest {

    @Autowired MockMvc mockMvc;

    @MockBean TeacherService teacherService;
    @MockBean TeacherMapper teacherMapper; 


    @MockBean com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;
    @MockBean com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt unauthorizedHandler;
    @MockBean com.openclassrooms.starterjwt.security.jwt.JwtUtils jwtUtils;
    @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String,String>> handle(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "unexpected"));
        }
    }

    @Test @DisplayName("GET /api/teacher/{id} -> 404 si service renvoie null")
    void getById_notFound() throws Exception {
        Mockito.when(teacherService.findById(42L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", 42))
               .andExpect(status().isNotFound());
    }

    @Test @DisplayName("GET /api/teacher/{id} -> 400 si id non numérique")
    void getById_badRequest_whenIdNotNumeric() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "abc"))
               .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("GET /api/teacher/{id} -> 500 si RuntimeException")
    void getById_unexpectedError() throws Exception {
        Mockito.when(teacherService.findById(anyLong()))
               .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/teacher/{id}", 7))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.error").value("unexpected"));
    }
}
