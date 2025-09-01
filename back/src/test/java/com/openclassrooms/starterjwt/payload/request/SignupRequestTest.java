package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    @Test
    @DisplayName("Getters/Setters + toString non nul")
    void getters_setters_and_toString() {
        SignupRequest r = new SignupRequest();
        r.setEmail("new@test.com");
        r.setPassword("pwd123");
        r.setFirstName("Alice");
        r.setLastName("Wonder");
        
        try {
           
            SignupRequest.class.getMethod("setAdmin", boolean.class).invoke(r, true);
        } catch (ReflectiveOperationException ignored) {}

        assertThat(r.getEmail()).isEqualTo("new@test.com");
        assertThat(r.getPassword()).isEqualTo("pwd123");
        assertThat(r.getFirstName()).isEqualTo("Alice");
        assertThat(r.getLastName()).isEqualTo("Wonder");
        assertThat(r.toString()).isNotNull();
    }
}
