package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherDtoTouchTest {

    @Test
    void teacherDto_getters_setters_equals_hash_toString() {
        TeacherDto t = new TeacherDto();
        t.setId(7L);
        t.setFirstName("Jane");
        t.setLastName("Guru");

        assertThat(t.getId()).isEqualTo(7L);
        assertThat(t.getFirstName()).isEqualTo("Jane");
        assertThat(t.getLastName()).isEqualTo("Guru");

        TeacherDto same = new TeacherDto();
        same.setId(7L);
        same.setFirstName("Jane");
        same.setLastName("Guru");

        assertThat(t).isEqualTo(same);
        assertThat(t.hashCode()).isEqualTo(same.hashCode());

        same.setLastName("Other");
        assertThat(t).isNotEqualTo(same);

        assertThat(t.toString()).contains("Jane");
    }
}
