package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestBoundaryTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        validator = f.getValidator();
    }

    
    private static String pad(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    @Test
    @DisplayName("SignupRequest: null fields -> violations")
    void nullFields_produceViolations() {
        SignupRequest r = new SignupRequest();
        r.setEmail(null);
        r.setPassword(null);
        r.setFirstName(null);
        r.setLastName(null);

        Set<ConstraintViolation<SignupRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }

    @Test
    @DisplayName("SignupRequest: blanks -> violations (NotBlank)")
    void blankValues_produceViolations() {
        SignupRequest r = new SignupRequest();
        r.setEmail("  ");
        r.setPassword(" ");
        r.setFirstName(" ");
        r.setLastName(" ");

        Set<ConstraintViolation<SignupRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }

    @Test
    @DisplayName("SignupRequest: invalid email -> violations (@Email)")
    void badEmail_producesViolation() {
        SignupRequest r = new SignupRequest();
        r.setEmail("bad.email");
        r.setPassword("secret123");
        r.setFirstName("Alice");
        r.setLastName("Doe");

        Set<ConstraintViolation<SignupRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }

    @Test
    @DisplayName("SignupRequest: too long names/password -> violations (@Size) + getters/toString")
    void tooLongNamesPassword_produceViolations_and_toString() {
        SignupRequest r = new SignupRequest();
        r.setEmail("alice@example.com");
        r.setPassword(pad('p', 300));          
        r.setFirstName(pad('a', 200));
        r.setLastName(pad('b', 200));

        Set<ConstraintViolation<SignupRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();

       
        assertThat(r.getEmail()).isEqualTo("alice@example.com");
        assertThat(r.getPassword()).hasSize(300);
        assertThat(r.getFirstName()).hasSize(200);
        assertThat(r.getLastName()).hasSize(200);
        assertThat(r.toString()).isNotNull();
    }
}
