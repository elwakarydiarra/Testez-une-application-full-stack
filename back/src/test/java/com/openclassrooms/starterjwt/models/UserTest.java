package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("Builder et getters/setters basiques")
    void builder_getters_setters() {
        LocalDateTime now = LocalDateTime.now();

        User u = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Wonder")
                .email("alice@example.com")
                .password("secret")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, u.getId());
        assertEquals("Alice", u.getFirstName());
        assertEquals("Wonder", u.getLastName());
        assertEquals("alice@example.com", u.getEmail());
        assertTrue(u.isAdmin());

        // setters
        u.setFirstName("A.");
        assertEquals("A.", u.getFirstName());
    }

    @Test
    @DisplayName("Equals/HashCode basés sur id")
    void equals_hashcode_on_id() {
        User u1 = User.builder()
                .id(10L)
                .firstName("John")
                .lastName("Doe")
                .email("a@a.com")
                .password("pwd")
                .build();

        User u2 = User.builder()
                .id(10L)
                .firstName("Jane")
                .lastName("Doe")
                .email("b@b.com")
                .password("pwd2")
                .build();

        User u3 = User.builder()
                .id(11L)
                .firstName("Jim")
                .lastName("Beam")
                .email("c@c.com")
                .password("pwd3")
                .build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1, u3);
    }

    @Test
    @DisplayName("toString n'est pas null et contient des champs clés")
    void toString_contains_fields() {
        User u = User.builder()
                .id(99L)
                .firstName("Bob")
                .lastName("Marley")
                .email("bob@marley.com")
                .password("oneLove")
                .build();

        String s = u.toString();
        assertNotNull(s);
        assertTrue(s.contains("Bob"));
        assertTrue(s.contains("Marley"));
        assertTrue(s.contains("bob@marley.com"));
    }

    @Test
    @DisplayName("Constructeur par défaut : valeurs null / par défaut")
    void default_constructor_defaults() {
        User u = new User();
        assertNull(u.getId());
        assertNull(u.getEmail());
        assertFalse(u.isAdmin());
    }
}
