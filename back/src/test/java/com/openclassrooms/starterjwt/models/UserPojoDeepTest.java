package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserPojoDeepTest {

    @Test
    void setters_getters_and_toString() {
        User u = new User();

        u.setId(99L);
        u.setEmail("test@test.com");
        u.setFirstName("Jean");
        u.setLastName("Dupont");
        u.setPassword("secret");
        u.setAdmin(true);

        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now();

        u.setCreatedAt(created);
        u.setUpdatedAt(updated);

        assertThat(u.getId()).isEqualTo(99L);
        assertThat(u.getEmail()).isEqualTo("test@test.com");
        assertThat(u.getFirstName()).isEqualTo("Jean");
        assertThat(u.getLastName()).isEqualTo("Dupont");
        assertThat(u.getPassword()).isEqualTo("secret");
        assertThat(u.isAdmin()).isTrue();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);

        String ts = u.toString();
        assertThat(ts).isNotNull();
        assertThat(ts).contains("Jean").contains("Dupont");
    }

    @Test
    void equals_and_hashCode_use_id() {
        User a = new User();
        User b = new User();

        a.setId(5L);
        b.setId(5L);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        b.setId(10L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_is_stable_when_id_is_null() {
        User u = new User();
        int h1 = u.hashCode();
        int h2 = u.hashCode();
        assertThat(h1).isEqualTo(h2);

        assertThat(u.toString()).isNotNull();
    }
}
