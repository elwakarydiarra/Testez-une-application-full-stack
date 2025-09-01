package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserBuilderAndAccessorsTest {

    @Test
    void builder_and_getters_setters_work() {
        User u = User.builder()
                .id(42L)
                .email("john@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("pwd")
                .admin(true)
                .build();

        assertThat(u.getId()).isEqualTo(42L);
        assertThat(u.getEmail()).isEqualTo("john@test.com");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getFirstName()).isEqualTo("John");
        assertThat(u.isAdmin()).isTrue();

        u.setFirstName("Jane").setLastName("Roe");
        assertThat(u.getFirstName()).isEqualTo("Jane");
        assertThat(u.getLastName()).isEqualTo("Roe");
    }
}
