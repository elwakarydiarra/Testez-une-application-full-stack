package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestEqualsHashCodeTest {

    @Test
    void equals_sameObject_returnsTrue() {
        SignupRequest req = new SignupRequest();
        req.setEmail("x@test.com");
        assertThat(req.equals(req)).isTrue();
    }

    @Test
    void equals_nullObject_returnsFalse() {
        SignupRequest req = new SignupRequest();
        assertThat(req.equals(null)).isFalse();
    }

    @Test
    void equals_differentType_returnsFalse() {
        SignupRequest req = new SignupRequest();
        assertThat(req.equals("string")).isFalse();
    }

    @Test
    void equals_differentFields_returnsFalse() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@test.com");
        r1.setFirstName("A");
        r1.setLastName("B");
        r1.setPassword("secret");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("b@test.com"); 
        r2.setFirstName("A");
        r2.setLastName("B");
        r2.setPassword("secret");

        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void equals_sameFields_returnsTrue_andHashCodesMatch() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@test.com");
        r1.setFirstName("A");
        r1.setLastName("B");
        r1.setPassword("secret");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("a@test.com");
        r2.setFirstName("A");
        r2.setLastName("B");
        r2.setPassword("secret");

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void hashCode_stable_whenCalledMultipleTimes() {
        SignupRequest r = new SignupRequest();
        r.setEmail("a@test.com");
        int h1 = r.hashCode();
        int h2 = r.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void canEqual_shouldWorkWithSameType() {
        SignupRequest r1 = new SignupRequest();
        SignupRequest r2 = new SignupRequest();
        assertThat(r1.canEqual(r2)).isTrue();
    }

    @Test
    void canEqual_shouldReturnFalseWithDifferentType() {
        SignupRequest r1 = new SignupRequest();
        Object other = new Object();
        assertThat(r1.canEqual(other)).isFalse();
    }
}
