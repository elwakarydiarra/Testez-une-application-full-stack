package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@Import(UserControllerMoreErrorsTest.TestExceptionHandler.class)
class UserControllerMoreErrorsTest {

    @Autowired
    private MockMvc mockMvc;

   
    @MockBean private UserService userService;
    @MockBean private UserMapper userMapper;

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
    @DisplayName("GET /api/user/{id} -> 400 quand id non numérique")
    void getById_badRequest_whenIdNotNumeric() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/user/{id} -> 404 quand utilisateur absent")
    void getById_notFound() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/user/{id} -> 500 quand exception inattendue")
    void getById_unexpectedError() throws Exception {
        when(userService.findById(7L)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/user/{id}", 7))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("unexpected"));
    }

  

    @Test
    @DisplayName("DELETE /api/user/{id} -> 400 quand id non numérique")
    void delete_badRequest_whenIdNotNumeric() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "xyz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} -> 404 quand utilisateur absent (correction)")
    void delete_notFound() throws Exception {
        when(userService.findById(77L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/{id}", 77))
                .andExpect(status().isNotFound());

        verify(userService).findById(77L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} -> 500 quand exception inattendue")
    void delete_unexpectedError() throws Exception {
        when(userService.findById(10L)).thenReturn(new User());
        doThrow(new RuntimeException("oops")).when(userService).delete(10L);

        mockMvc.perform(delete("/api/user/{id}", 10))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("unexpected"));
    }
}
