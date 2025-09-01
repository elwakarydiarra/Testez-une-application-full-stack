package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.jpa.JpaAuditingAutoConfiguration"
})
class TeacherControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    
    @MockBean private TeacherService teacherService;
    @MockBean private TeacherMapper teacherMapper;

   
    @MockBean private com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt unauthorizedHandler;
    @MockBean private com.openclassrooms.starterjwt.security.jwt.JwtUtils jwtUtils;

    @MockBean org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMappingContext;

    @Test
    @DisplayName("GET /api/teacher -> 200 avec liste vide")
    void findAll_returnsTeachers() throws Exception {
        Mockito.when(teacherService.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/teacher/{id} -> 200 quand trouvé")
    void findById_returnsTeacher() throws Exception {
        Mockito.when(teacherService.findById(1L)).thenReturn(new Teacher());

        TeacherDto dto = new TeacherDto();
        
        Mockito.when(teacherMapper.toDto(any(Teacher.class))).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }
}
