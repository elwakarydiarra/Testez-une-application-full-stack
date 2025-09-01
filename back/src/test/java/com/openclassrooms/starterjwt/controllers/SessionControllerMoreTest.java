package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
class SessionControllerMoreTest {

    private static final String BASE = "/api/session";
    private static final String CREATE_URL = BASE;                       
    private static final String UPDATE_URL = BASE + "/{id}";            
    private static final String PARTICIPATE_URL = BASE + "/{sid}/participate/{uid}";        
    private static final String UNPARTICIPATE_URL = BASE + "/{sid}/participate/{uid}";     

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean SessionService sessionService;
    @MockBean SessionMapper sessionMapper;

    @MockBean com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;
    @MockBean com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt unauthorizedHandler;
    @MockBean com.openclassrooms.starterjwt.security.jwt.JwtUtils jwtUtils;
    @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

    private Session entity(Long id) {
        return Session.builder()
                .id(id)
                .name("Morning Yoga")
                .description("Sun salutations")
                .date(new Date())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST participate -> 200 OK")
    void participate_shouldReturnOk() throws Exception {
        long sid = 1L, uid = 9L;
        doNothing().when(sessionService).participate(sid, uid);

        mvc.perform(post(PARTICIPATE_URL, String.valueOf(sid), String.valueOf(uid)))
                .andExpect(status().isOk());

        verify(sessionService).participate(sid, uid);
    }

    @Test
    @DisplayName("DELETE participate -> 200 OK (désinscription)")
    void noLongerParticipate_shouldReturnOk() throws Exception {
        long sid = 2L, uid = 10L;
        doNothing().when(sessionService).noLongerParticipate(sid, uid);

        mvc.perform(delete(UNPARTICIPATE_URL, String.valueOf(sid), String.valueOf(uid)))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(sid, uid);
    }

    @Test
    @DisplayName("POST /api/session -> map DTO -> service.create -> retourne DTO")
    void create_shouldMapDto_thenServiceCreate_thenReturnDto() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Morning Yoga");
        payload.put("description", "Sun salutations");
        payload.put("date", new Date());          
        payload.put("teacher_id", 5L);           
        payload.put("users", Collections.emptyList());

       
        Session saved = entity(42L);

       
        SessionDto returned = new SessionDto();
        returned.setId(42L);
        returned.setName("Morning Yoga");
        returned.setDescription("Sun salutations");
        returned.setDate(saved.getDate());
        returned.setTeacher_id(5L);
        returned.setUsers(Collections.emptyList());
        returned.setCreatedAt(saved.getCreatedAt());
        returned.setUpdatedAt(saved.getUpdatedAt());

        
        given(sessionMapper.toEntity(any(SessionDto.class))).willReturn(entity(null));
        given(sessionService.create(any(Session.class))).willReturn(saved);
        given(sessionMapper.toDto(any(Session.class))).willReturn(returned);

        mvc.perform(post(CREATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Morning Yoga"));
    }

    @Test
    @DisplayName("PUT /api/session/{id} -> map DTO -> service.update -> retourne DTO")
    void update_shouldMapDto_thenServiceUpdate_thenReturnDto() throws Exception {
        long id = 7L;

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("name", "Morning Yoga");
        payload.put("description", "Updated");
        payload.put("date", new Date());
        payload.put("teacher_id", 5L);
        payload.put("users", Collections.emptyList());

        Session updated = entity(id);
        updated.setDescription("Updated");

        SessionDto returned = new SessionDto();
        returned.setId(id);
        returned.setName("Morning Yoga");
        returned.setDescription("Updated");
        returned.setDate(updated.getDate());
        returned.setTeacher_id(5L);
        returned.setUsers(Collections.emptyList());
        returned.setCreatedAt(updated.getCreatedAt());
        returned.setUpdatedAt(updated.getUpdatedAt());

        given(sessionMapper.toEntity(any(SessionDto.class))).willReturn(entity(id));
        given(sessionService.update(eq(id), any(Session.class))).willReturn(updated);
        given(sessionMapper.toDto(any(Session.class))).willReturn(returned);

        mvc.perform(put(UPDATE_URL, String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.description").value("Updated"));
    }
}
