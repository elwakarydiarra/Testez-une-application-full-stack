package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ToStringSmokeTest {

  @Test
  void user_toString_ok() {
    User u = User.builder()
        .email("u@test.com")
        .firstName("U")
        .lastName("Ser")
        .password("pwd")
        .admin(false)
        .build();
    assertThat(u.toString()).isNotBlank();
  }

  @Test
  void teacher_toString_ok() {
    Teacher t = Teacher.builder().firstName("T").lastName("One").build();
    assertThat(t.toString()).isNotBlank();
  }

  @Test
  void session_toString_ok() {
    Session s = Session.builder().name("S").build();
    assertThat(s.toString()).isNotBlank();
  }
}
