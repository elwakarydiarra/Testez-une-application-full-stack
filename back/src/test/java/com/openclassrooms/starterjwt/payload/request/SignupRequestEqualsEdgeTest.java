package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestEqualsEdgeTest {

    @Test
    void equals_sameReference_shouldReturnTrue() {
        SignupRequest r = new SignupRequest();
        r.setEmail("a@a.com");
        r.setFirstName("A");
        r.setLastName("B");
        r.setPassword("p");

       
        assertThat(r.equals(r)).isTrue();
    }

    @Test
    void equals_differentClass_shouldReturnFalse() {
        SignupRequest r = new SignupRequest();
        r.setEmail("a@a.com");
        r.setFirstName("A");
        r.setLastName("B");
        r.setPassword("p");

        
        assertThat(r.equals("une chaîne")).isFalse();
    }
}
