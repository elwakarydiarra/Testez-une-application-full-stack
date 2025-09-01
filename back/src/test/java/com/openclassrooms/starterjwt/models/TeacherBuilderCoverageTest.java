package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherBuilderCoverageTest {

    @Test
    void builder_toString_notNull() {
        String ts = Teacher.builder()
                .id(1L)
                .firstName("Alan")
                .lastName("Turing")
                .toString();

        assertThat(ts).isNotBlank();
        assertThat(ts).contains("Alan").contains("Turing");
    }
}
