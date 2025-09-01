package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestEqualsBranchesTest {

    static class SignupRequestNeverEqual extends SignupRequest {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    private static SignupRequest req(String email, String fn, String ln, String pwd) {
        SignupRequest r = new SignupRequest();
        r.setEmail(email);
        r.setFirstName(fn);
        r.setLastName(ln);
        r.setPassword(pwd);
        return r;
    }

    @Test
    void equals_hashCode_and_canEqual_branches() {
        SignupRequest a = req("x@y.z", "Mini", "Test", "pwd");

        
        assertThat(a.equals(a)).isTrue();
        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("nope")).isFalse();

        
        SignupRequest b = req("x2@y.z", "Mini", "Test", "pwd");
        assertThat(a.equals(b)).isFalse();

        
        SignupRequest c = req("x@y.z", "Mini", "Test", "pwd");
        assertThat(a.equals(c)).isTrue();
        assertThat(a.hashCode()).isEqualTo(c.hashCode());
        assertThat(a.toString()).contains("x@y.z");

        
        SignupRequestNeverEqual child = new SignupRequestNeverEqual();
        child.setEmail("x@y.z");
        child.setFirstName("Mini");
        child.setLastName("Test");
        child.setPassword("pwd");

        assertThat(a.equals(child)).isFalse(); 
        assertThat(child.equals(a)).isTrue(); 
    }
}
