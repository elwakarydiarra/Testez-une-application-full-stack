package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsHashEdgeCasesTest {

  @Test
  void user_equals_handlesNullAndDifferentClass() {
    User u1 = User.builder().id(1L).email("a@b.c").password("p").firstName("A").lastName("B").admin(false).build();
    User u2 = User.builder().id(1L).email("x@y.z").password("q").firstName("X").lastName("Y").admin(true).build();
    User uNull = User.builder().email("n@n.n").password("p").firstName("N").lastName("N").admin(false).build();

    assertThat(u1).isEqualTo(u1);
    assertThat(u1).isEqualTo(u2);
    assertThat(u1).hasSameHashCodeAs(u2);
    assertThat(u1).isNotEqualTo(uNull);
    assertThat(u1).isNotEqualTo("not a user");
    assertThat(u1.equals(null)).isFalse();
  }

  @Test
  void teacher_equals_and_hash() {
    Teacher t1 = Teacher.builder().id(7L).firstName("T").lastName("1").build();
    Teacher t2 = Teacher.builder().id(7L).firstName("Other").lastName("Name").build();
    Teacher t3 = Teacher.builder().id(8L).firstName("T").lastName("1").build();

    assertThat(t1).isEqualTo(t2);
    assertThat(t1).hasSameHashCodeAs(t2);
    assertThat(t1).isNotEqualTo(t3);
  }

  @Test
  void session_equals_and_hash_withNullIds_followLombokContract() {
    Session s1 = Session.builder().name("A").build();
    Session s2 = Session.builder().name("A").build();

    assertThat(s1).isEqualTo(s2);
  }
}
