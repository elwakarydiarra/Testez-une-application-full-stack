package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserSetterBranchesTest {

    @Test
    void setters_withNonNull_values() {
        User u = new User();

        u.setEmail("user@test.com");
        u.setLastName("Last");
        u.setFirstName("First");
        u.setPassword("pwd");

        assertThat(u.getEmail()).isEqualTo("user@test.com");
        assertThat(u.getLastName()).isEqualTo("Last");
        assertThat(u.getFirstName()).isEqualTo("First");
        assertThat(u.getPassword()).isEqualTo("pwd");
    }

    @Test
    void setters_withNull_values_throw_NPE_covering_null_path() {
        User u = new User();

       
        assertThrows(NullPointerException.class, () -> u.setEmail(null));
        assertThrows(NullPointerException.class, () -> u.setLastName(null));
        assertThrows(NullPointerException.class, () -> u.setFirstName(null));
        assertThrows(NullPointerException.class, () -> u.setPassword(null));
    }
}
