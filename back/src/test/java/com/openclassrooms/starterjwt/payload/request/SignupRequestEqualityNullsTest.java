package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SignupRequestEqualityNullsTest {

    @Test
    void equals_oneFieldNullInOneObject_shouldReturnFalse() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail(null);            
        r1.setFirstName("Alice");
        r1.setLastName("Wonder");
        r1.setPassword("secret");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("alice@test.com");  
        r2.setFirstName("Alice");
        r2.setLastName("Wonder");
        r2.setPassword("secret");

        assertThat(r1).isNotEqualTo(r2);
       
        assertThat(r2).isNotEqualTo(r1);
    }

    @Test
    void equals_allFieldsNull_shouldReturnTrue_and_hashCodeStable() {
        SignupRequest r1 = new SignupRequest();
        SignupRequest r2 = new SignupRequest(); 

       
        assertThat(r1).isEqualTo(r2).isEqualTo(r1);

       
        int h1 = r1.hashCode();
        int h2 = r1.hashCode();
        int hOther = r2.hashCode();
        assertThat(h1).isEqualTo(h2);
        assertThat(h1).isEqualTo(hOther);
    }
}
