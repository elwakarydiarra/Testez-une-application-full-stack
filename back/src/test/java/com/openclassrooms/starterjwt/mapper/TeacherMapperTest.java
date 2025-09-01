package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {

    private final TeacherMapper mapper = new TeacherMapper();

    @Test
    void toDto_maps_basic_fields() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        TeacherDto dto = mapper.toDto(teacher);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toEntity_maps_basic_fields() {
        LocalDateTime t1 = LocalDateTime.now().minusDays(1);
        LocalDateTime t2 = LocalDateTime.now();

        TeacherDto dto = new TeacherDto();
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setCreatedAt(t1);
        dto.setUpdatedAt(t2);

        Teacher entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getFirstName()).isEqualTo("Jane");
        assertThat(entity.getLastName()).isEqualTo("Smith");
        assertThat(entity.getCreatedAt()).isEqualTo(t1);
        assertThat(entity.getUpdatedAt()).isEqualTo(t2);
    }

    @Test
    void toDto_list_and_toEntity_list() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Alan");
        teacher1.setLastName("Turing");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Ada");
        teacher2.setLastName("Lovelace");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        List<TeacherDto> dtos = mapper.toDto(teachers);
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getFirstName()).isEqualTo("Alan");
        assertThat(dtos.get(1).getFirstName()).isEqualTo("Ada");

        List<Teacher> backToEntities = mapper.toEntity(dtos);
        assertThat(backToEntities).hasSize(2);
        assertThat(backToEntities.get(0).getLastName()).isEqualTo("Turing");
        assertThat(backToEntities.get(1).getLastName()).isEqualTo("Lovelace");
    }

    @Test
    void null_safety() {
        assertThat(mapper.toDto((Teacher) null)).isNull();
        assertThat(mapper.toEntity((TeacherDto) null)).isNull();
        assertThat(mapper.toDto((List<Teacher>) null)).isNull();
        assertThat(mapper.toEntity((List<TeacherDto>) null)).isNull();
    }
}
