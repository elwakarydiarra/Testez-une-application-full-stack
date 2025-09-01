package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEqualsHashEdgeCasesTest {

    private static User userWithId(long id) {
        User u = new User();
        u.setId(id);
        u.setEmail("alice@test.com");
        u.setFirstName("Alice");
        u.setLastName("Wonder");
        u.setPassword("pwd");
        u.setAdmin(false);
        return u;
    }

    @Test
    void equals_sameReference_true() {
        User u = userWithId(1L);
        assertThat(u.equals(u)).isTrue();
    }

    @Test
    void equals_null_false() {
        User u = userWithId(1L);
        assertThat(u.equals(null)).isFalse();
    }

    @Test
    void equals_differentType_false() {
        User u = userWithId(1L);
        assertThat(u.equals("not-a-user")).isFalse();
    }

    @Test
    void equals_sameId_true() {
        User a = userWithId(7L);
        User b = userWithId(7L);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equals_differentId_false() {
        User a = userWithId(7L);
        User b = userWithId(8L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_stable_whenIdNull() {
        User u = new User();                
        int h1 = u.hashCode();
        int h2 = u.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void toString_notNull_and_containsHints() {
        User u = userWithId(99L);
        assertThat(u.toString()).isNotNull()
                .contains("Alice")         
                .contains("Wonder");
    }
}
