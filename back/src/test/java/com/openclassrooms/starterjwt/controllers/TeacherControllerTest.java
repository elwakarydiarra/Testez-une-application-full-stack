package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TeacherController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = WebSecurityConfig.class
        )
)
@AutoConfigureMockMvc(addFilters = false)

@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Test
    @DisplayName("GET /api/teacher renvoie la liste des teachers")
    void findAll_returnsTeachers() throws Exception {
        Teacher t1 = Teacher.builder().id(1L).firstName("John").lastName("Doe")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        Teacher t2 = Teacher.builder().id(2L).firstName("Jane").lastName("Roe")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        List<Teacher> entities = Arrays.asList(t1, t2);

        TeacherDto d1 = new TeacherDto();
        d1.setId(1L); d1.setFirstName("John"); d1.setLastName("Doe");

        TeacherDto d2 = new TeacherDto();
        d2.setId(2L); d2.setFirstName("Jane"); d2.setLastName("Roe");

        List<TeacherDto> dtos = Arrays.asList(d1, d2);

        Mockito.when(teacherService.findAll()).thenReturn(entities);
        Mockito.when(teacherMapper.toDto(eq(entities))).thenReturn(dtos);
        Mockito.when(teacherMapper.toDto(any(Teacher.class))).thenAnswer(inv -> {
            Teacher t = inv.getArgument(0);
            TeacherDto dto = new TeacherDto();
            dto.setId(t.getId());
            dto.setFirstName(t.getFirstName());
            dto.setLastName(t.getLastName());
            return dto;
        });

        mockMvc.perform(get("/api/teacher").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    @DisplayName("GET /api/teacher/{id} -> 200 avec teacher")
    void findById_returnsTeacher() throws Exception {
        long id = 7L;
        Teacher entity = Teacher.builder().id(id).firstName("Alice").lastName("Smith")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        TeacherDto dto = new TeacherDto();
        dto.setId(id); dto.setFirstName("Alice"); dto.setLastName("Smith");

        Mockito.when(teacherService.findById(id)).thenReturn(entity);
        Mockito.when(teacherMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    @DisplayName("GET /api/teacher/{id} -> 404 si non trouvé")
    void findById_returnsNotFound() throws Exception {
        long id = 99L;
        Mockito.when(teacherService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/teacher/{id} -> 400 si id invalide")
    void findById_returnsBadRequest_whenInvalidId() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
