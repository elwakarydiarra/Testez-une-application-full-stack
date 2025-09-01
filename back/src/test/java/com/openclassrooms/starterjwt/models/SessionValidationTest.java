package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SessionValidationTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void missing_description_triggers_violation() {
        Session s = Session.builder()
                .name("Yoga doux")
                .date(new Date())
                .description(null) 
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Session>> v = validator.validate(s);
        assertThat(v).anyMatch(cv -> "description".equals(cv.getPropertyPath().toString()));
    }

    @Test
    void valid_session_has_no_violations() {
        Session s = Session.builder()
                .name("Vinyasa")
                .date(new Date())
                .description("Flow du soir")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Session>> v = validator.validate(s);
        assertThat(v).isEmpty();
    }
}
