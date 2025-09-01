package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelToStringAndEqualsContractTest {

    private User u(long id) {
        return User.builder()
                .id(id)
                .email("u" + id + "@test.com")
                .lastName("Last" + id)
                .firstName("First" + id)
                .password("pwd" + id)
                .admin(false)
                .build();
    }

    private Teacher t(long id) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName("TFirst" + id);
        teacher.setLastName("TLast" + id);
        return teacher;
    }

    private Session s(long id) {
        Session s = new Session();
        s.setId(id);
        s.setName("S" + id);
        s.setDescription("D" + id);
        return s;
    }

    @Test
    void equals_and_hashCode_sameId_meansEqual_regardlessOfOtherFields() {
        User u1 = u(1L);
        User u2 = u(1L);
        u2.setFirstName("changed");

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());

        Teacher te1 = t(7L);
        Teacher te2 = t(7L);
        te2.setLastName("changed");
        assertThat(te1).isEqualTo(te2);
        assertThat(te1.hashCode()).isEqualTo(te2.hashCode());

        Session s1 = s(9L);
        Session s2 = s(9L);
        s2.setDescription("changed");
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }

    @Test
    void equals_withDifferentIds_isFalse() {
        assertThat(u(1L)).isNotEqualTo(u(2L));
        assertThat(t(1L)).isNotEqualTo(t(2L));
        assertThat(s(1L)).isNotEqualTo(s(2L));
    }

    @Test
    void equals_withNullId_behavior_matches_lombok_ofId_contract() {
        User uNull1 = u(0L); uNull1.setId(null);
        User uNull2 = u(0L); uNull2.setId(null);
        assertThat(uNull1).isEqualTo(uNull2);

        Teacher tNull1 = t(0L); tNull1.setId(null);
        Teacher tNull2 = t(0L); tNull2.setId(null);
        assertThat(tNull1).isEqualTo(tNull2);

        Session sNull1 = s(0L); sNull1.setId(null);
        Session sNull2 = s(0L); sNull2.setId(null);
        assertThat(sNull1).isEqualTo(sNull2);
    }

    @Test
    void equals_selfAndOtherType_andNull() {
        User u = u(3L);
        assertThat(u).isEqualTo(u);          
        assertThat(u).isNotEqualTo(null);   
        assertThat(u).isNotEqualTo("string"); 

        Teacher te = t(4L);
        assertThat(te).isEqualTo(te);
        assertThat(te).isNotEqualTo(null);
        assertThat(te).isNotEqualTo(123);

        Session s = s(5L);
        assertThat(s).isEqualTo(s);
        assertThat(s).isNotEqualTo(null);
        assertThat(s).isNotEqualTo(new Object());
    }

    @Test
    void toString_isNonNull_and_containsKeyFields() {
        User u = u(11L);
        assertThat(u.toString()).isNotNull().contains("u11@test.com");

        Teacher te = t(12L);
        assertThat(te.toString()).isNotNull().contains("TFirst12").contains("TLast12");

        Session s = s(13L);
        assertThat(s.toString()).isNotNull().contains("S13").contains("D13");
    }
}
