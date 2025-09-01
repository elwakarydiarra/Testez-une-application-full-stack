package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserConstructorAndEqualsTest {

    @Test
    void constructor_withId_andFullFields_setsEverything() {
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now();

        
        User u = new User(
                42L,
                "alice@test.com",
                "Wonder",    
                "Alice",     
                "pwd",
                true,
                created,
                updated
        );

        assertThat(u.getId()).isEqualTo(42L);
        assertThat(u.getEmail()).isEqualTo("alice@test.com");
        assertThat(u.getFirstName()).isEqualTo("Alice");
        assertThat(u.getLastName()).isEqualTo("Wonder");
        assertThat(u.getPassword()).isEqualTo("pwd");
        assertThat(u.isAdmin()).isTrue();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
        assertThat(u.toString()).isNotNull();
    }

    @Test
    void constructor_shortForm_fromAuthController_signature_setsFields() {
        
        User u = new User("bob@test.com", "Bob", "Builder", "secret", false);

        assertThat(u.getId()).isNull();
        assertThat(u.getEmail()).isEqualTo("bob@test.com");
        assertThat(u.getLastName()).isEqualTo("Bob");
        assertThat(u.getFirstName()).isEqualTo("Builder");
        assertThat(u.getPassword()).isEqualTo("secret");
        assertThat(u.isAdmin()).isFalse();
        assertThat(u.getCreatedAt()).isNull();
        assertThat(u.getUpdatedAt()).isNull();
        assertThat(u.toString()).isNotNull();
    }

    @Test
    void equals_and_hashCode_various_cases() {
        User a = new User();
        User b = new User();

        
        assertThat(a.equals(a)).isTrue();

        
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        
        a.setId(7L);
        b.setId(8L);
        assertThat(a).isNotEqualTo(b);

        
        b.setId(7L);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        
        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("not-a-user")).isFalse();
    }
}
