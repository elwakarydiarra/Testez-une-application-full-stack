package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserExtraCoverageTest {

    @Test
    void allConstructors_shouldInitializeFields() {
        LocalDateTime now = LocalDateTime.now();

       
        User u1 = new User(1L, "a@a.com", "Wonder", "Alice", "pwd", true, now, now);

        assertThat(u1.getId()).isEqualTo(1L);
        assertThat(u1.getEmail()).isEqualTo("a@a.com");
        assertThat(u1.getLastName()).isEqualTo("Wonder");
        assertThat(u1.getFirstName()).isEqualTo("Alice");
        assertThat(u1.getPassword()).isEqualTo("pwd");
        assertThat(u1.isAdmin()).isTrue();
        assertThat(u1.getCreatedAt()).isEqualTo(now);
        assertThat(u1.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void builder_shouldWorkForAllFields() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        User u = User.builder()
                .id(2L)
                .email("bob@test.com")
                .lastName("Doe")
                .firstName("Bob")
                .password("secret")
                .admin(false)
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getEmail()).isEqualTo("bob@test.com");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getFirstName()).isEqualTo("Bob");
        assertThat(u.getPassword()).isEqualTo("secret");
        assertThat(u.isAdmin()).isFalse();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
    }
}
