package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional 
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User persisted;

    @BeforeEach
    void setUp() {
        User u = User.builder()
                .email("jane@yoga.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("secret")
                .admin(false)
                .build();

        persisted = userRepository.save(u);
        assertThat(persisted.getId()).isNotNull();
    }

    @Test
    void findById_returnsPersistedUser() {
        User found = userService.findById(persisted.getId());

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("jane@yoga.com");
        assertThat(found.getFirstName()).isEqualTo("Jane");
        assertThat(found.getLastName()).isEqualTo("Doe");
    }

    @Test
    void delete_removesUser() {
        Long id = persisted.getId();

        userService.delete(id);

        User after = userService.findById(id);
        assertThat(after).isNull();
    }
}
