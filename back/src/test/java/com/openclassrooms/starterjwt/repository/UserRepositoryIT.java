package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail_andExistsByEmail() {
        User user = User.builder()
                .email("test@yoga.com") 
                .firstName("Test")
                .lastName("User")
                .password("pwd")
                .admin(false)
                .build();

        userRepository.save(user);

        assertThat(userRepository.findByEmail("test@yoga.com")).isPresent();
        assertThat(userRepository.existsByEmail("test@yoga.com")).isTrue();
        assertThat(userRepository.existsByEmail("unknown@yoga.com")).isFalse();
    }
}
