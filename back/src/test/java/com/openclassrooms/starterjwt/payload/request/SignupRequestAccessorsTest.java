package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestAccessorsTest {
    @Test
    void all_accessors_and_toString() {
        SignupRequest r = new SignupRequest();
        r.setEmail("alice@test.com");
        r.setPassword("pwd123");
        r.setFirstName("Alice");
        r.setLastName("Doe");

        assertThat(r.getEmail()).isEqualTo("alice@test.com");
        assertThat(r.getPassword()).isEqualTo("pwd123");
        assertThat(r.getFirstName()).isEqualTo("Alice");
        assertThat(r.getLastName()).isEqualTo("Doe");
        assertThat(r.toString()).isNotBlank();

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("alice@test.com");
        r2.setPassword("pwd123");
        r2.setFirstName("Alice");
        r2.setLastName("Doe");
        assertThat(r).isEqualTo(r2);
        assertThat(r.hashCode()).isEqualTo(r2.hashCode());
    }
}
