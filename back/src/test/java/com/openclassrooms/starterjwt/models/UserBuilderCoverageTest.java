package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserBuilderCoverageTest {

    @Test
    void builder_all_setters_toString_and_build() {
        LocalDateTime created = LocalDateTime.now().minusDays(3);
        LocalDateTime updated = LocalDateTime.now();

        User.UserBuilder b = User.builder()
                .id(42L)
                .email("builder@test.com")
                .firstName("Ada")
                .lastName("Lovelace")
                .password("pwd")
                .admin(true)
                .createdAt(created)
                .updatedAt(updated);

        
        String builderStr = b.toString();
        assertThat(builderStr).isNotNull().contains("Ada").contains("builder@test.com");

       
        User u = b.build();
        assertThat(u.getId()).isEqualTo(42L);
        assertThat(u.getEmail()).isEqualTo("builder@test.com");
        assertThat(u.getFirstName()).isEqualTo("Ada");
        assertThat(u.getLastName()).isEqualTo("Lovelace");
        assertThat(u.getPassword()).isEqualTo("pwd");
        assertThat(u.isAdmin()).isTrue();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
    }
}
