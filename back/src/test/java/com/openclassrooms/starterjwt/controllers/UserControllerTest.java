package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockBean(name = "jpaAuditingHandler")
    private AuditingHandler jpaAuditingHandler;

    @Test
    @DisplayName("GET /api/user/{id} -> 200 et renvoie l'utilisateur")
    void findById_returnsUser() throws Exception {
        long id = 5L;
        LocalDateTime now = LocalDateTime.now();

        User entity = User.builder()
                .id(id)
                .email("jane.doe@example.com")
                .lastName("Doe")
                .firstName("Jane")
                .password("secret")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail("jane.doe@example.com");
        dto.setLastName("Doe");
        dto.setFirstName("Jane");
        dto.setAdmin(false);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        Mockito.when(userService.findById(id)).thenReturn(entity);
        Mockito.when(userMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/user/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.email", is("jane.doe@example.com")))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    @DisplayName("GET /api/user/{id} -> 404 si non trouvé")
    void findById_returnsNotFound() throws Exception {
        long id = 999L;

        Mockito.when(userService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/user/{id} -> 400 si id invalide")
    void findById_returnsBadRequest_whenInvalidId() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
