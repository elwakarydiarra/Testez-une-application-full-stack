package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
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
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DtoThroughMapperSmokeTest {

    private final TeacherService teacherService = Mockito.mock(TeacherService.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final SessionMapper sessionMapper = new SessionMapper(teacherService, userService);

    @Test
    @DisplayName("UserDto : égalité/hashCode + toString (valeurs non null)")
    void userDto_roundtrip_and_equality() {
        UserDto a = new UserDto();
        a.setId(1L);
        a.setEmail("alice@test.com");
        a.setFirstName("Alice");
        a.setLastName("Wonder");

        // On vérifie les champs utiles plutôt que d’attendre un objet vide
        assertThat(a.getId()).isEqualTo(1L);
        assertThat(a.getEmail()).isEqualTo("alice@test.com");
        assertThat(a.getFirstName()).isEqualTo("Alice");
        assertThat(a.getLastName()).isEqualTo("Wonder");

        // equals/hashCode basés sur TOUS les champs (lombok @Data) → créer un “clone” identique
        UserDto same = new UserDto();
        same.setId(1L);
        same.setEmail("alice@test.com");
        same.setFirstName("Alice");
        same.setLastName("Wonder");

        UserDto diff = new UserDto();
        diff.setId(2L);
        diff.setEmail("bob@test.com");
        diff.setFirstName("Bob");
        diff.setLastName("Doe");

        assertThat(a).isEqualTo(same);
        assertThat(a.hashCode()).isEqualTo(same.hashCode());
        assertThat(a).isNotEqualTo(diff);

        assertThat(a.toString()).contains("alice@test.com", "Alice", "Wonder");
    }

    @Test
    @DisplayName("TeacherDto : égalité/hashCode + toString (valeurs non null)")
    void teacherDto_roundtrip_and_equality() {
        TeacherDto a = new TeacherDto();
        a.setId(10L);
        a.setFirstName("John");
        a.setLastName("Doe");

        assertThat(a.getId()).isEqualTo(10L);
        assertThat(a.getFirstName()).isEqualTo("John");
        assertThat(a.getLastName()).isEqualTo("Doe");

        TeacherDto same = new TeacherDto();
        same.setId(10L);
        same.setFirstName("John");
        same.setLastName("Doe");

        TeacherDto diff = new TeacherDto();
        diff.setId(11L);
        diff.setFirstName("Jane");
        diff.setLastName("Roe");

        assertThat(a).isEqualTo(same);
        assertThat(a.hashCode()).isEqualTo(same.hashCode());
        assertThat(a).isNotEqualTo(diff);

        assertThat(a.toString()).contains("John", "Doe");
    }

    @Test
    @DisplayName("SessionDto : round-trip via SessionMapper (valeurs non null attendues)")
    void sessionDto_roundtrip_through_sessionMapper_and_equality() {
        SessionDto dto = new SessionDto();
        dto.setId(42L);
        dto.setName("Morning Flow");
        dto.setDescription("Yoga session");
        dto.setDate(new Date());
        dto.setTeacher_id(100L);
        dto.setUsers(Arrays.asList(200L, 201L));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // stubs pour la résolution dans SessionMapper
        Teacher t = new Teacher(); t.setId(100L);
        User u200 = new User(); u200.setId(200L);
        User u201 = new User(); u201.setId(201L);
        when(teacherService.findById(100L)).thenReturn(t);
        when(userService.findById(200L)).thenReturn(u200);
        when(userService.findById(201L)).thenReturn(u201);

        // dto -> entity
        Session entity = sessionMapper.toEntity(dto);
        assertThat(entity.getId()).isEqualTo(42L);
        assertThat(entity.getName()).isEqualTo("Morning Flow");
        assertThat(entity.getDescription()).isEqualTo("Yoga session");
        assertThat(entity.getTeacher()).isNotNull();
        assertThat(entity.getTeacher().getId()).isEqualTo(100L);
        assertThat(entity.getUsers()).extracting("id").containsExactlyInAnyOrder(200L, 201L);
        assertThat(entity.getDate()).isNotNull();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();

        // entity -> dto
        SessionDto roundtrip = sessionMapper.toDto(entity);
        assertThat(roundtrip.getId()).isEqualTo(42L);
        assertThat(roundtrip.getName()).isEqualTo("Morning Flow");
        assertThat(roundtrip.getDescription()).isEqualTo("Yoga session");
        assertThat(roundtrip.getTeacher_id()).isEqualTo(100L);
        assertThat(roundtrip.getUsers()).containsExactlyInAnyOrder(200L, 201L);
        assertThat(roundtrip.getDate()).isNotNull();
        assertThat(roundtrip.getCreatedAt()).isNotNull();
        assertThat(roundtrip.getUpdatedAt()).isNotNull();

        // equals/hashCode (lombok @Data => tous champs) : créer un clone identique
        SessionDto same = new SessionDto();
        same.setId(42L);
        same.setName("Morning Flow");
        same.setDescription("Yoga session");
        same.setDate(roundtrip.getDate());
        same.setTeacher_id(100L);
        same.setUsers(Arrays.asList(200L, 201L));
        same.setCreatedAt(roundtrip.getCreatedAt());
        same.setUpdatedAt(roundtrip.getUpdatedAt());

        SessionDto diff = new SessionDto();
        diff.setId(99L);

        assertThat(roundtrip).isEqualTo(same);
        assertThat(roundtrip.hashCode()).isEqualTo(same.hashCode());
        assertThat(roundtrip).isNotEqualTo(diff);

        assertThat(roundtrip.toString()).contains("Morning Flow");
    }
}
