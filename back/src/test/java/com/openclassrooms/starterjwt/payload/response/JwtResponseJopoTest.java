package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtResponseJopoTest {

    @Test
    void full_constructor_and_getters_setters() {
        JwtResponse r = new JwtResponse("tok", 1L, "user@test.com", "Alice", "Doe", true);

        assertThat(r.getToken()).isEqualTo("tok");
        assertThat(r.getType()).isEqualTo("Bearer");
        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.getUsername()).isEqualTo("user@test.com");
        assertThat(r.getFirstName()).isEqualTo("Alice");
        assertThat(r.getLastName()).isEqualTo("Doe");
        assertThat(r.getAdmin()).isTrue();

        r.setToken("tok2");
        r.setUsername("u2@test.com");
        r.setFirstName("A2");
        r.setLastName("D2");
        r.setAdmin(false);

        assertThat(r.getToken()).isEqualTo("tok2");
        assertThat(r.getUsername()).isEqualTo("u2@test.com");
        assertThat(r.getFirstName()).isEqualTo("A2");
        assertThat(r.getLastName()).isEqualTo("D2");
        assertThat(r.getAdmin()).isFalse();
    }
}
