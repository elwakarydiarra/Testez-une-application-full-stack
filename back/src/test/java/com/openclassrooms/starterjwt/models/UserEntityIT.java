package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserEntityIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Persist & find User (mapping basique)")
    void persist_and_find_user() {
        LocalDateTime now = LocalDateTime.now();

        User toSave = User.builder()
                .firstName("Alice")
                .lastName("Wonder")
                .email("alice@example.com")
                .password("secret")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User saved = userRepository.save(toSave);
        assertThat(saved.getId()).isNotNull();

        User found = userRepository.findById(saved.getId())
                .orElseThrow(() -> new AssertionError("User not found: id=" + saved.getId())); 
        assertThat(found.getEmail()).isEqualTo("alice@example.com");
        assertThat(found.isAdmin()).isTrue();
        assertThat(found.getFirstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Equals/HashCode basés sur id (après persistance)")
    void equals_hashcode_on_id() {
        User u1 = userRepository.save(User.builder()
                .firstName("A").lastName("A").email("a@a.com").password("x").build());
        User u2 = userRepository.save(User.builder()
                .firstName("B").lastName("B").email("b@b.com").password("y").build());

        u2.setId(u1.getId());

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }
}
