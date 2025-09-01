package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperTest {

    private SessionMapper sessionMapper;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);
        sessionMapper = new SessionMapper(teacherService, userService);
    }

    @Test
    @DisplayName("toDto mappe correctement un Session vers SessionDto")
    void toDto_mapsEntityToDto() {
        Teacher t = new Teacher();
        t.setId(5L);

        User u1 = new User(); u1.setId(1L);
        User u2 = new User(); u2.setId(2L);

        Session s = new Session();
        s.setId(10L);
        s.setName("Yoga");
        s.setDescription("Relaxation");
        s.setDate(new Date());
        s.setTeacher(t);
        s.setUsers(Arrays.asList(u1, u2));

        SessionDto dto = sessionMapper.toDto(s);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Yoga", dto.getName());
        assertEquals("Relaxation", dto.getDescription());
        assertEquals(5L, dto.getTeacher_id());
        assertEquals(Arrays.asList(1L, 2L), dto.getUsers());
    }

    @Test
    @DisplayName("toEntity mappe correctement un SessionDto vers Session")
    void toEntity_mapsDtoToEntity() {
        SessionDto dto = new SessionDto();
        dto.setId(20L);
        dto.setName("Pilates");
        dto.setDescription("Core strength");
        dto.setDate(new Date());
        dto.setTeacher_id(7L);
        dto.setUsers(Collections.singletonList(100L));

        Teacher fakeTeacher = new Teacher(); fakeTeacher.setId(7L);
        User fakeUser = new User(); fakeUser.setId(100L);

        when(teacherService.findById(7L)).thenReturn(fakeTeacher);
        when(userService.findById(100L)).thenReturn(fakeUser);

        Session s = sessionMapper.toEntity(dto);

        assertNotNull(s);
        assertEquals(20L, s.getId());
        assertEquals("Pilates", s.getName());
        assertEquals("Core strength", s.getDescription());
        assertNotNull(s.getTeacher());
        assertEquals(7L, s.getTeacher().getId());
        assertEquals(1, s.getUsers().size());
        assertEquals(100L, s.getUsers().get(0).getId());
    }

    @Test
    @DisplayName("toDto(null) et toEntity(null) retournent null")
    void nullInputs_returnNull() {
        assertNull(sessionMapper.toDto((Session) null));
        assertNull(sessionMapper.toEntity((SessionDto) null));
        assertNull(sessionMapper.toEntity((List<SessionDto>) null));
        assertNull(sessionMapper.toDto((List<Session>) null));
    }

    @Test
    @DisplayName("Le mapping de liste (entities -> dtos) fonctionne")
    void listMapping_entitiesToDtos() {
        Session s1 = new Session(); s1.setId(1L); s1.setUsers(Collections.<User>emptyList());
        Session s2 = new Session(); s2.setId(2L); s2.setUsers(Collections.<User>emptyList());

        List<SessionDto> dtos = sessionMapper.toDto(Arrays.asList(s1, s2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
    }
}
