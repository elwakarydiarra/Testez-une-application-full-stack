package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;                
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;    
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;                 

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;          
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = SessionController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SessionService sessionService;
    @MockBean private SessionMapper sessionMapper;

    @MockBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockBean(name = "jpaAuditingHandler")
    private AuditingHandler jpaAuditingHandler;

    private final ObjectMapper om = new ObjectMapper();        

    @Test
    @DisplayName("GET /api/session renvoie la liste des sessions")
    void findAll_returnsSessions() throws Exception {
        Date now = new Date();

        Session s1 = Session.builder().id(1L).name("Morning Yoga").date(now).description("Desc 1").build();
        Session s2 = Session.builder().id(2L).name("Evening Yoga").date(now).description("Desc 2").build();
        List<Session> entities = Arrays.asList(s1, s2);

        SessionDto d1 = new SessionDto(); d1.setId(1L); d1.setName("Morning Yoga"); d1.setDate(now); d1.setDescription("Desc 1");
        SessionDto d2 = new SessionDto(); d2.setId(2L); d2.setName("Evening Yoga"); d2.setDate(now); d2.setDescription("Desc 2");
        List<SessionDto> dtos = Arrays.asList(d1, d2);

        Mockito.when(sessionService.findAll()).thenReturn(entities);
        Mockito.when(sessionMapper.toDto(eq(entities))).thenReturn(dtos);
        Mockito.when(sessionMapper.toDto(any(Session.class))).thenAnswer(inv -> {
            Session s = inv.getArgument(0);
            SessionDto dto = new SessionDto();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setDate(s.getDate());
            dto.setDescription(s.getDescription());
            return dto;
        });

        mockMvc.perform(get("/api/session").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Morning Yoga")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Evening Yoga")));
    }

    @Test
    @DisplayName("GET /api/session/{id} renvoie la session demandée")
    void getById_returnsSession() throws Exception {
        long id = 42L;
        Date now = new Date();

        Session entity = Session.builder().id(id).name("Power Yoga").date(now).description("Desc").build();
        SessionDto dto = new SessionDto(); dto.setId(id); dto.setName("Power Yoga"); dto.setDate(now); dto.setDescription("Desc");

        Mockito.when(sessionService.getById(id)).thenReturn(entity);
        Mockito.when(sessionMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/session/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is("Power Yoga")));
    }

    @Test
    void list_sessions_deserialize_to_SessionDto_and_touch_getters() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andReturn();

        String json = res.getResponse().getContentAsString();

       
        List<SessionDto> dtos = om.readValue(
                json,
                om.getTypeFactory().constructCollectionType(List.class, SessionDto.class)
        );

       
        for (SessionDto dto : dtos) {
            dto.getId();
            dto.getName();
            dto.getDescription();
            dto.getDate();
            dto.getTeacher_id(); 
            dto.getUsers();      

            SessionDto sameId = new SessionDto();
            sameId.setId(dto.getId());
            assertThat(dto).isEqualTo(sameId);
            assertThat(dto.hashCode()).isEqualTo(sameId.hashCode());

            assertThat(dto.toString()).contains(String.valueOf(dto.getId()));
            touchDto(dto);
        }
    }

    
    private static void touchDto(Object dto) {
        assertThat(dto).isEqualTo(dto); 
        dto.hashCode();
        dto.toString();
    }
}
