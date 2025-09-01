package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherEqualityTest {

    @Test
    void equals_true_whenSameId() {
        Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(1L).firstName("X").lastName("Y").build();

        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
    }

    @Test
    void equals_false_whenDifferentId() {
        Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(2L).firstName("A").lastName("B").build();

        assertThat(t1).isNotEqualTo(t2);
    }

    @Test
    void equals_false_whenOneIdNull_otherNotNull() {
        Teacher t1 = Teacher.builder().id(null).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(2L).firstName("A").lastName("B").build();

        assertThat(t1).isNotEqualTo(t2);
        assertThat(t2).isNotEqualTo(t1);
    }

    @Test
    void equals_true_whenBothIdsNull() {
        Teacher t1 = Teacher.builder().id(null).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(null).firstName("A").lastName("B").build();

        
        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
    }
}
