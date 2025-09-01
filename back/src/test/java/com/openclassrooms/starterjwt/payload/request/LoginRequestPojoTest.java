package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestPojoTest {

    @Test
    void getters_setters_and_toString() {
        LoginRequest r = new LoginRequest();
        r.setEmail("user@test.com");
        r.setPassword("secret");

        assertThat(r.getEmail()).isEqualTo("user@test.com");
        assertThat(r.getPassword()).isEqualTo("secret");
       
        assertThat(r.toString()).isNotNull();
    }
}
