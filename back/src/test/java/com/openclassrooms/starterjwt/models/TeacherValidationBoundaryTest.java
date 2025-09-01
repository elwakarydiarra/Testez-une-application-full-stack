package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherValidationBoundaryTest {

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

    private static Teacher base() {
        return Teacher.builder()
                .firstName("A")
                .lastName("B")
                .build();
    }

    @Test @DisplayName("firstName: 20 OK, 21 KO")
    void firstName_boundary() {
        Teacher ok = base().setFirstName(ofLen(20));
        Teacher ko = base().setFirstName(ofLen(21));
        assertThat(validator.validate(ok)).isEmpty();
        assertThat(validator.validate(ko)).isNotEmpty();
    }

    @Test @DisplayName("lastName: 20 OK, 21 KO")
    void lastName_boundary() {
        Teacher ok = base().setLastName(ofLen(20));
        Teacher ko = base().setLastName(ofLen(21));
        assertThat(validator.validate(ok)).isEmpty();
        assertThat(validator.validate(ko)).isNotEmpty();
    }

    @Test @DisplayName("blank → violations")
    void blank_names_invalid() {
        Teacher t = base().setFirstName(" ").setLastName(" ");
        assertThat(validator.validate(t)).isNotEmpty();
    }
}
