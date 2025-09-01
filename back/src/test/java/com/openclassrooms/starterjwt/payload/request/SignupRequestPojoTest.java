package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestPojoTest {

    @Test
    void getters_setters_and_toString() {
        SignupRequest r = new SignupRequest();
        r.setEmail("alice@test.com");
        r.setPassword("pwd");
        r.setFirstName("Alice");
        r.setLastName("Doe");

        assertThat(r.getEmail()).isEqualTo("alice@test.com");
        assertThat(r.getPassword()).isEqualTo("pwd");
        assertThat(r.getFirstName()).isEqualTo("Alice");
        assertThat(r.getLastName()).isEqualTo("Doe");
        assertThat(r.toString()).isNotNull();
    }
}
