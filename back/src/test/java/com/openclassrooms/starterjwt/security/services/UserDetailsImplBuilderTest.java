package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplBuilderToStringTest {

  @Test
  void builder_toString_isCovered() {
  
    UserDetailsImpl.UserDetailsImplBuilder b = UserDetailsImpl.builder()
        .id(42L)
        .username("alice@test.com")
        .firstName("Alice")
        .lastName("Wonder")
        .admin(true)
        .password("secret");

  
    String s = b.toString();

    assertThat(s).isNotNull();
   
    assertThat(s)
        .contains("id")
        .contains("username")
        .contains("firstName")
        .contains("lastName");
  }
}
