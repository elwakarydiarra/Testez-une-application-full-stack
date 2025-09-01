package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTeacher_hasNoViolations() {
        Teacher teacher = Teacher.builder()
                .firstName("Marie")
                .lastName("Curie")
                .build();

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).isEmpty();
    }

    @Test
    void tooLongNames_triggerSizeViolations() {
        
        String longName = new String(new char[60]).replace('\0', 'A');

        Teacher teacher = Teacher.builder()
                .firstName(longName)
                .lastName(longName)
                .build();

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        
        Set<String> violatedProps = violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(violations).isNotEmpty();
        assertThat(violatedProps).anyMatch(p -> p.equals("firstName") || p.equals("lastName"));
    }
}
