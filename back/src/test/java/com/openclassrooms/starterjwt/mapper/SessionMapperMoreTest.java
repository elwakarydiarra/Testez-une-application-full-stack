package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class SessionMapperMoreTest {

    private final TeacherService teacherService = Mockito.mock(TeacherService.class);
    private final UserService userService = Mockito.mock(UserService.class);

    private final SessionMapper mapper = new SessionMapper(teacherService, userService);

    @Test
    @DisplayName("entity -> dto : tous les champs et collect des IDs utilisateurs")
    void entity_to_dto_full() {
        Session s = new Session();
        s.setId(10L);
        s.setName("Yoga Morning");
        s.setDescription("desc");
        s.setDate(new Date());
        s.setCreatedAt(LocalDateTime.now().minusDays(1));
        s.setUpdatedAt(LocalDateTime.now());

        Teacher t = new Teacher();
        t.setId(100L);
        t.setFirstName("Guru");
        t.setLastName("Ji");
        s.setTeacher(t);

        User u1 = new User(); u1.setId(200L);
        User u2 = new User(); u2.setId(201L);
        s.setUsers(Arrays.asList(u1, u2));

        SessionDto dto = mapper.toDto(s);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("Yoga Morning");
        assertThat(dto.getDescription()).isEqualTo("desc");
        assertThat(dto.getTeacher_id()).isEqualTo(100L);
        assertThat(dto.getUsers()).containsExactlyInAnyOrder(200L, 201L);
        assertThat(dto.getDate()).isNotNull();
        assertThat(dto.getCreatedAt()).isNotNull();
        assertThat(dto.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("dto -> entity : résolution teacher & users via services (findById)")
    void dto_to_entity_withUsers_andTeacher() {
        SessionDto dto = new SessionDto();
        dto.setId(42L);
        dto.setName("Evening");
        dto.setDescription("desc");
        dto.setDate(new Date());
        dto.setCreatedAt(LocalDateTime.now().minusHours(2));
        dto.setUpdatedAt(LocalDateTime.now().minusHours(1));
        dto.setTeacher_id(99L);
        dto.setUsers(Arrays.asList(10L, 11L));

        Teacher teacher = new Teacher();
        teacher.setId(99L);

        User u10 = new User(); u10.setId(10L);
        User u11 = new User(); u11.setId(11L);

        // Stubs robustes
        when(teacherService.findById(99L)).thenReturn(teacher);
        when(userService.findById(10L)).thenReturn(u10);
        when(userService.findById(11L)).thenReturn(u11);

        // Default pour éviter "UnfinishedStubbing" ou NullPointer
        when(userService.findById(anyLong())).thenAnswer(inv -> {
            Long id = inv.getArgument(0);
            if (id == 10L) return u10;
            if (id == 11L) return u11;
            return null;
        });

        Session entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(42L);
        assertThat(entity.getName()).isEqualTo("Evening");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getTeacher()).isNotNull();
        assertThat(entity.getTeacher().getId()).isEqualTo(99L);
        assertThat(entity.getUsers()).extracting("id").containsExactlyInAnyOrder(10L, 11L);
        assertThat(entity.getDate()).isNotNull();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("List<Session> -> List<SessionDto> et inversement (avec stubs minimum)")
    void list_mapping_both_ways() {
        Session s1 = new Session(); s1.setId(1L);
        Session s2 = new Session(); s2.setId(2L);

        List<SessionDto> dtos = mapper.toDto(Arrays.asList(s1, s2));
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting("id").containsExactly(1L, 2L);

        SessionDto d1 = new SessionDto(); d1.setId(10L); d1.setTeacher_id(null); d1.setUsers(null);
        SessionDto d2 = new SessionDto(); d2.setId(11L); d2.setTeacher_id(null); d2.setUsers(Collections.emptyList());

        List<Session> entities = mapper.toEntity(Arrays.asList(d1, d2));
        assertThat(entities).hasSize(2);
        assertThat(entities).extracting("id").containsExactly(10L, 11L);
    }

    @Test
    @DisplayName("Null-safety : toEntity(null)/toDto(null) et listes null")
    void null_safety() {
        assertThat(mapper.toEntity((SessionDto) null)).isNull();
        assertThat(mapper.toDto((Session) null)).isNull();

        assertThat(mapper.toEntity((List<SessionDto>) null)).isNull();
        assertThat(mapper.toDto((List<Session>) null)).isNull();
    }
}
