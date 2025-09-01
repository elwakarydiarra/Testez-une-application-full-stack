package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionEqualityTest {

    @Test
    void equals_true_whenSameId() {
        Session s1 = Session.builder().id(10L).name("S1").build();
        Session s2 = Session.builder().id(10L).name("S2").build();

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }

    @Test
    void equals_false_whenDifferentId() {
        Session s1 = Session.builder().id(10L).name("S1").build();
        Session s2 = Session.builder().id(11L).name("S1").build();

        assertThat(s1).isNotEqualTo(s2);
    }

    @Test
    void equals_false_whenOneIdNull_otherNotNull() {
        Session s1 = Session.builder().id(null).name("S1").build();
        Session s2 = Session.builder().id(11L).name("S1").build();

        assertThat(s1).isNotEqualTo(s2);
        assertThat(s2).isNotEqualTo(s1);
    }

    @Test
    void equals_true_whenBothIdsNull() {
        Session s1 = Session.builder().id(null).name("S1").build();
        Session s2 = Session.builder().id(null).name("S2").build();

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }
}
