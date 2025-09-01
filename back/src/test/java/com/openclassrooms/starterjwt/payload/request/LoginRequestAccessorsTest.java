package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestAccessorsTest {

    @Test
    void all_accessors_and_toString() {
        LoginRequest r = new LoginRequest();
        r.setEmail("user@test.com");
        r.setPassword("secret");

        assertThat(r.getEmail()).isEqualTo("user@test.com");
        assertThat(r.getPassword()).isEqualTo("secret");
       
        assertThat(r.toString()).isNotBlank();

        
        LoginRequest r2 = new LoginRequest();
        r2.setEmail("user@test.com");
        r2.setPassword("secret");
        assertThat(r2).isNotSameAs(r); 
    }
}
