package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserConstructorBranchesTest {

    @Test
    void fullConstructor_withNonNullDates_coversBranch() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        User u = new User(
                123L,
                "alice@test.com",
                "Doe",
                "Alice",
                "pwd",
                true,
                created,
                updated
        );

        assertThat(u.getId()).isEqualTo(123L);
        assertThat(u.getEmail()).isEqualTo("alice@test.com");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getFirstName()).isEqualTo("Alice");
        assertThat(u.getPassword()).isEqualTo("pwd");
        assertThat(u.isAdmin()).isTrue();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void fullConstructor_withNullDates_coversOtherBranch() {
       
        User u = new User(
                456L,
                "bob@test.com",
                "Bob",
                "Bobby",
                "secret",
                false,
                null,
                null
        );

        assertThat(u.getId()).isEqualTo(456L);
        assertThat(u.getEmail()).isEqualTo("bob@test.com");
        assertThat(u.getLastName()).isEqualTo("Bob");
        assertThat(u.getFirstName()).isEqualTo("Bobby");
        assertThat(u.getPassword()).isEqualTo("secret");
        assertThat(u.isAdmin()).isFalse();
        
        assertThat(u.getCreatedAt()).isNull();
        assertThat(u.getUpdatedAt()).isNull();
    }

    @Test
    void shortConstructor_coversBothSides_booleanAndStrings() {
        
        User u1 = new User("c@test.com", "C", "Céline", "pass", true);
        assertThat(u1.getEmail()).isEqualTo("c@test.com");
        assertThat(u1.getLastName()).isEqualTo("C");
        assertThat(u1.getFirstName()).isEqualTo("Céline");
        assertThat(u1.getPassword()).isEqualTo("pass");
        assertThat(u1.isAdmin()).isTrue();

        User u2 = new User("d@test.com", "D", "Dany", "x", false);
        assertThat(u2.getEmail()).isEqualTo("d@test.com");
        assertThat(u2.getLastName()).isEqualTo("D");
        assertThat(u2.getFirstName()).isEqualTo("Dany");
        assertThat(u2.getPassword()).isEqualTo("x");
        assertThat(u2.isAdmin()).isFalse();
    }
}
