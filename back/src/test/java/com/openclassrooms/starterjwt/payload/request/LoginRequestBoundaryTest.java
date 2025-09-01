package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestBoundaryTest {

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
    @DisplayName("LoginRequest: null fields -> violations (NotBlank)")
    void nullFields_produceViolations() {
        LoginRequest r = new LoginRequest();
        r.setEmail(null);
        r.setPassword(null);

        Set<ConstraintViolation<LoginRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }

    @Test
    @DisplayName("LoginRequest: blank values -> violations (NotBlank)")
    void blankValues_produceViolations() {
        LoginRequest r = new LoginRequest();
        r.setEmail("   ");
        r.setPassword("  ");

        Set<ConstraintViolation<LoginRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }

    @Test
    @DisplayName("LoginRequest: email au format libre -> 0 violation si seule contrainte NotBlank")
    void weirdEmail_isAccepted_whenNoEmailConstraint() {
        LoginRequest r = new LoginRequest();
        r.setEmail("pas-un-email"); 
        r.setPassword("secret");

        Set<ConstraintViolation<LoginRequest>> v = validator.validate(r);
        assertThat(v).isEmpty();
        assertThat(r.toString()).isNotNull();
    }

    @Test
    @DisplayName("LoginRequest: valeurs très longues -> 0 violation si pas de @Size")
    void veryLongValues_areAccepted_whenNoSizeConstraint() {
        String longLocal = pad('a', 200);
        String longDomain = pad('b', 200) + ".com";
        String longEmail = longLocal + "@" + longDomain;

        LoginRequest r = new LoginRequest();
        r.setEmail(longEmail);
        r.setPassword(pad('x', 1000));

        Set<ConstraintViolation<LoginRequest>> v = validator.validate(r);
       
        assertThat(v).isEmpty();

     
        assertThat(r.getEmail()).isEqualTo(longEmail);
        assertThat(r.getPassword()).hasSize(1000);
        assertThat(r.toString()).isNotNull();
    }
}
