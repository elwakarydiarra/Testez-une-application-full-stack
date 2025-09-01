package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherPojoDeepTest {

    @Test
    void setters_getters_and_toString() {
        Teacher t = new Teacher();

        t.setId(42L);
        t.setFirstName("Marie");
        t.setLastName("Curie");

        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        t.setCreatedAt(created);
        t.setUpdatedAt(updated);

        assertThat(t.getId()).isEqualTo(42L);
        assertThat(t.getFirstName()).isEqualTo("Marie");
        assertThat(t.getLastName()).isEqualTo("Curie");
        assertThat(t.getCreatedAt()).isEqualTo(created);
        assertThat(t.getUpdatedAt()).isEqualTo(updated);

        String ts = t.toString();
        assertThat(ts).isNotNull();
        assertThat(ts).contains("Marie").contains("Curie");
    }

    @Test
    void equals_and_hashCode_use_id() {
        Teacher a = new Teacher();
        Teacher b = new Teacher();

        a.setId(1L);
        b.setId(1L);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        b.setId(2L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_is_stable_when_id_is_null() {
        Teacher t = new Teacher();
        int h1 = t.hashCode();
        int h2 = t.hashCode();
        assertThat(h1).isEqualTo(h2);

        assertThat(t.toString()).isNotNull();
    }
}
