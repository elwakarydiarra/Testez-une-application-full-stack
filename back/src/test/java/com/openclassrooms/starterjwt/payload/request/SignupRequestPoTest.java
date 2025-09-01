package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestPoTest {

    @Test
    void equals_and_hashCode_shouldCoverAllBranches() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("a@test.com");
        req1.setFirstName("Alice");
        req1.setLastName("Wonder");
        req1.setPassword("secret");

        
        assertThat(req1.equals(req1)).isTrue();

        
        assertThat(req1.equals(null)).isFalse();

       
        assertThat(req1.equals("not a request")).isFalse();

       
        SignupRequest req2 = new SignupRequest();
        req2.setEmail("a@test.com");
        req2.setFirstName("Alice");
        req2.setLastName("Wonder");
        req2.setPassword("secret");

        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());

     
        SignupRequest req3 = new SignupRequest();
        req3.setEmail("b@test.com");
        req3.setFirstName("Alice");
        req3.setLastName("Wonder");
        req3.setPassword("secret");

        assertThat(req1).isNotEqualTo(req3);
        assertThat(req1.hashCode()).isNotEqualTo(req3.hashCode());

        
        int h1 = req1.hashCode();
        int h2 = req1.hashCode();
        assertThat(h1).isEqualTo(h2);
    }
}
