package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidationBoundaryTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    private static String ofLen(int n, char c) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    private static User base() {
        
        return User.builder()
                .email("a@b.cd")               
                .firstName("A")
                .lastName("B")
                .password("p")
                .admin(false)
                .build();
    }

    @Test @DisplayName("email: 50 OK, 51 KO")
    void email_boundary() {
        User u50 = base().setEmail(ofLen(50-4, 'x') + "@b.c"); 
        User u51 = base().setEmail(ofLen(51-4, 'x') + "@b.c"); 

        assertThat(validator.validate(u50)).isEmpty();
        assertThat(validator.validate(u51)).isNotEmpty();
    }

    @Test @DisplayName("firstName: 20 OK, 21 KO")
    void firstName_boundary() {
        User ok = base().setFirstName(ofLen(20, 'a'));
        User ko = base().setFirstName(ofLen(21, 'a'));
        assertThat(validator.validate(ok)).isEmpty();
        assertThat(validator.validate(ko)).isNotEmpty();
    }

    @Test @DisplayName("lastName: 20 OK, 21 KO")
    void lastName_boundary() {
        User ok = base().setLastName(ofLen(20, 'b'));
        User ko = base().setLastName(ofLen(21, 'b'));
        assertThat(validator.validate(ok)).isEmpty();
        assertThat(validator.validate(ko)).isNotEmpty();
    }

    @Test @DisplayName("password: 120 OK, 121 KO")
    void password_boundary() {
        User ok = base().setPassword(ofLen(120, 'p'));
        User ko = base().setPassword(ofLen(121, 'p'));
        assertThat(validator.validate(ok)).isEmpty();
        assertThat(validator.validate(ko)).isNotEmpty();
    }
}
