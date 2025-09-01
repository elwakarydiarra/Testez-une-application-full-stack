package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherBuilderExtraTest {

    @Test
    void teacherBuilder_shouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();

        Teacher t = Teacher.builder()
                .id(42L)
                .firstName("Marie")
                .lastName("Curie")
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        assertThat(t.getId()).isEqualTo(42L);
        assertThat(t.getFirstName()).isEqualTo("Marie");
        assertThat(t.getLastName()).isEqualTo("Curie");
        assertThat(t.getCreatedAt()).isNotNull();
        assertThat(t.getUpdatedAt()).isNotNull();
    }

    @Test
    void toString_shouldContainNames() {
        Teacher t = new Teacher();
        t.setFirstName("Albert");
        t.setLastName("Einstein");

        String ts = t.toString();
        assertThat(ts).contains("Albert").contains("Einstein");
    }
}
