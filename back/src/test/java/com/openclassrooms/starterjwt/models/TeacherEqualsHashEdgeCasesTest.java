package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherEqualsHashEdgeCasesTest {

    private static Teacher teacherWithId(long id) {
        Teacher t = new Teacher();
        t.setId(id);
        t.setFirstName("Yogi");
        t.setLastName("Guru");
        return t;
    }

    @Test
    void equals_sameReference_true() {
        Teacher t = teacherWithId(1L);
        assertThat(t.equals(t)).isTrue();
    }

    @Test
    void equals_null_false() {
        Teacher t = teacherWithId(1L);
        assertThat(t.equals(null)).isFalse();
    }

    @Test
    void equals_differentType_false() {
        Teacher t = teacherWithId(1L);
        assertThat(t.equals(123)).isFalse();
    }

    @Test
    void equals_sameId_true_and_hashMatches() {
        Teacher a = teacherWithId(5L);
        Teacher b = teacherWithId(5L);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equals_differentId_false() {
        Teacher a = teacherWithId(5L);
        Teacher b = teacherWithId(6L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_stable_whenIdNull() {
        Teacher t = new Teacher();         
        int h1 = t.hashCode();
        int h2 = t.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void toString_notNull_and_containsHints() {
        Teacher t = teacherWithId(10L);
        assertThat(t.toString()).isNotNull()
                .contains("Yogi")
                .contains("Guru");
    }
}
