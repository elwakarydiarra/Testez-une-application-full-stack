package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    @DisplayName("Builder et getters/setters")
    void builder_getters_setters() {
        LocalDateTime now = LocalDateTime.now();

        Teacher t = Teacher.builder()
                .id(1L)
                .firstName("Marie")
                .lastName("Curie")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, t.getId());
        assertEquals("Marie", t.getFirstName());
        assertEquals("Curie", t.getLastName());

        t.setLastName("C.");
        assertEquals("C.", t.getLastName());
    }

    @Test
    @DisplayName("Equals/HashCode basés sur id")
    void equals_hashcode_on_id() {
        Teacher t1 = Teacher.builder().id(7L).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(7L).firstName("X").lastName("Y").build();
        Teacher t3 = Teacher.builder().id(8L).firstName("Z").lastName("W").build();

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
    }

    @Test
    @DisplayName("toString n'est pas null et contient le nom")
    void toString_contains_lastName() {
        Teacher t = Teacher.builder().id(99L).firstName("Ada").lastName("Lovelace").build();
        String s = t.toString();
        assertNotNull(s);
        assertTrue(s.contains("Lovelace"));
    }

    @Test
    @DisplayName("Constructeur par défaut")
    void default_constructor_defaults() {
        Teacher t = new Teacher();
        assertNull(t.getId());
        assertNull(t.getFirstName());
        assertNull(t.getLastName());
    }
}
