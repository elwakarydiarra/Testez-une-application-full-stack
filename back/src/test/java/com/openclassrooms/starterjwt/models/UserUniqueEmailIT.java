package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@ActiveProfiles("test")
class UserUniqueEmailIT {

  @Autowired UserRepository repo;

  @Test
  void savingTwoUsers_withSameEmail_throwsConstraintViolation() {
    repo.save(User.builder()
        .email("dup@test.com").password("x").firstName("A").lastName("B").admin(false).build());

    org.assertj.core.api.Assertions.assertThatThrownBy(() ->
        repo.save(User.builder()
            .email("dup@test.com").password("y").firstName("C").lastName("D").admin(false).build())
    ).isInstanceOf(DataIntegrityViolationException.class);
  }
}
