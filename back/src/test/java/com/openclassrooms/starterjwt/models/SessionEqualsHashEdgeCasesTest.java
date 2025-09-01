package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class SessionEqualsHashEdgeCasesTest {

    private static Session sessionWithId(long id) {
        Session s = new Session();
        s.setId(id);
        s.setName("Morning Yoga");
        s.setDescription("Sun salutations");
        s.setDate(new Date());
        return s;
    }

    @Test
    void equals_sameReference_true() {
        Session s = sessionWithId(1L);
        assertThat(s.equals(s)).isTrue();
    }

    @Test
    void equals_null_false() {
        Session s = sessionWithId(1L);
        assertThat(s.equals(null)).isFalse();
    }

    @Test
    void equals_differentType_false() {
        Session s = sessionWithId(1L);
        assertThat(s.equals("not-a-session")).isFalse();
    }

    @Test
    void equals_sameId_true_and_hashMatches() {
        Session a = sessionWithId(42L);
        Session b = sessionWithId(42L);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equals_differentId_false() {
        Session a = sessionWithId(42L);
        Session b = sessionWithId(43L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_stable_whenIdNull() {
        Session s = new Session();        
        int h1 = s.hashCode();
        int h2 = s.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void toString_notNull_and_containsHints() {
        Session s = sessionWithId(7L);
        assertThat(s.toString()).isNotNull()
                .contains("Morning")
                .contains("Yoga");
    }
}
