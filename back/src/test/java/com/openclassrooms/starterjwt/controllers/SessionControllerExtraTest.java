package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.jpa.JpaAuditingAutoConfiguration"
})
@Import(SessionControllerExtraTest.TestExceptionHandler.class)
class SessionControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SessionService sessionService;
    @MockBean private SessionMapper sessionMapper;

    
    @MockBean private com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt unauthorizedHandler;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.JwtUtils jwtUtils;

   
    @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "unexpected"));
        }
    }

    @Test
    @DisplayName("GET /api/session -> 200 avec liste vide")
    void findAll_ok_emptyList() throws Exception {
        Mockito.when(sessionService.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(sessionMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/session/{id} -> 200 si trouvé")
    void getById_ok() throws Exception {
        Mockito.when(sessionService.getById(1L)).thenReturn(new Session());
        Mockito.when(sessionMapper.toDto(any(Session.class))).thenReturn(new SessionDto());

        mockMvc.perform(get("/api/session/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("GET /api/session/{id} -> 404 si absent")
    void getById_notFound() throws Exception {
        Mockito.when(sessionService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/session/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/session/{id} -> 400 si id non numérique")
    void getById_badRequest_whenIdNotNumeric() throws Exception {
        mockMvc.perform(get("/api/session/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/session/{id} -> 500 sur erreur inattendue")
    void getById_unexpectedError_returns500() throws Exception {
        Mockito.when(sessionService.getById(eq(7L))).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/session/{id}", 7))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("unexpected"));
    }
}
