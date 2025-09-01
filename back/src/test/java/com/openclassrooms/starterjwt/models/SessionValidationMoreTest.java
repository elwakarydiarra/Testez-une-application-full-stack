package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class SessionValidationMoreTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    private static String ofLen(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append('x');
        return sb.toString();
    }

    private static Session base() {
        return Session.builder()
                .name("Yoga")
                .description("Flow")
                .date(new Date())
                .build();
    }

    @Test @DisplayName("valid session → 0 violation")
    void valid_ok() {
        assertThat(validator.validate(base())).isEmpty();
    }

    @Test @DisplayName("nulls → violations")
    void nulls_ko() {
        Session s = new Session();
        assertThat(validator.validate(s)).isNotEmpty();
    }

    @Test @DisplayName("name/description boundary: exact length tolerated (si @Size)")
    void boundaries_tolerated() {
        Session s = base().setName(ofLen(50)).setDescription(ofLen(255));
        assertThat(validator.validate(s)).isEmpty();
    }
}
