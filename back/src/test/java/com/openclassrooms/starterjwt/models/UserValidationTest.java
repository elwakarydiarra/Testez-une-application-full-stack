package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidationTest {

    private final Validator validator;

    public UserValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void emailTooLong_triggersViolation() {
     
        String tooLongLocal = new String(new char[51]).replace("\0", "x");
        String longEmail = tooLongLocal + "@mail.com";

        User u = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email(longEmail)
                .password("pwd")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }

    @Test
    void validEmail_passesValidation() {
        User u = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@mail.com")
                .password("pwd")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertThat(violations).isEmpty();
    }
}
