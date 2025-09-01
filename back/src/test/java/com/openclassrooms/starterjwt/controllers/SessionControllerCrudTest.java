package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerCrudTest {

    @Resource
    private MockMvc mvc;

    @MockBean private SessionService sessionService;
    @MockBean private SessionMapper sessionMapper;

    @MockBean private com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt unauthorizedHandler;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.JwtUtils jwtUtils;
    @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private SessionDto validDto(Long id) {
        SessionDto dto = new SessionDto();
        dto.setId(id);
        dto.setName("Morning flow");
        dto.setDescription("desc ok");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setUsers(Collections.emptyList());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private Session entityFrom(SessionDto dto) {
        Session s = new Session();
        s.setId(dto.getId());
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        s.setDate(dto.getDate());
        s.setCreatedAt(dto.getCreatedAt());
        s.setUpdatedAt(dto.getUpdatedAt());
        s.setUsers(Collections.emptyList());
        return s;
    }

    @Test
    @DisplayName("POST /api/session -> 200 quand DTO valide")
    void create_ok() throws Exception {
        SessionDto in = validDto(null);
        Session entityIn = entityFrom(in);
        Session saved = entityFrom(in);
        saved.setId(42L);
        SessionDto out = validDto(42L);

        Mockito.when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(entityIn);
        Mockito.when(sessionService.create(any(Session.class))).thenReturn(saved);
        Mockito.when(sessionMapper.toDto(saved)).thenReturn(out);

        mvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Morning flow\",\"description\":\"desc ok\",\"date\":\""
                        + in.getDate().getTime() + "\",\"teacher_id\":1,\"users\":[] }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42));
    }

    @Test
    @DisplayName("PUT /api/session/{id} -> 200 quand DTO valide et session existante")
    void update_ok() throws Exception {
        Long id = 7L;
        SessionDto in = validDto(id);
        Session entityIn = entityFrom(in);
        Session updated = entityFrom(in);
        updated.setId(id);
        updated.setName("Updated");
        SessionDto out = validDto(id);
        out.setName("Updated");

        Mockito.when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(entityIn);
        Mockito.when(sessionService.update(eq(id), any(Session.class))).thenReturn(updated);
        Mockito.when(sessionMapper.toDto(updated)).thenReturn(out);

        mvc.perform(put("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":7,\"name\":\"Morning flow\",\"description\":\"desc ok\",\"date\":\""
                        + in.getDate().getTime() + "\",\"teacher_id\":1,\"users\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("DELETE /api/session/{id} -> 200 quand session existe")
    void delete_ok() throws Exception {
        Long id = 10L;
        Mockito.when(sessionService.getById(id)).thenReturn(new Session());
        Mockito.doNothing().when(sessionService).delete(id);

        mvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isOk());
    }
}
