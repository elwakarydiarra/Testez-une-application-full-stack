package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionEntityIT {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Persist Session avec Teacher et Participants (jointure PARTICIPATE)")
    void persist_session_with_teacher_and_participants() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = teacherRepository.save(
                Teacher.builder()
                        .firstName("T")
                        .lastName("One")
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );

        User u1 = userRepository.save(User.builder()
                .firstName("U1")
                .lastName("One")
                .email("u1@test.com")
                .password("x")
                .createdAt(now)
                .updatedAt(now)
                .build());

        User u2 = userRepository.save(User.builder()
                .firstName("U2")
                .lastName("Two")
                .email("u2@test.com")
                .password("y")
                .createdAt(now)
                .updatedAt(now)
                .build());

        Session s = Session.builder()
                .name("Morning Yoga")
                .description("Flow doux")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        s.getUsers().add(u1);
        s.getUsers().add(u2);

        Session saved = sessionRepository.save(s);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(saved.getUsers()).hasSize(2);

        Session found = sessionRepository.findById(saved.getId())
                .orElseThrow(() -> new AssertionError("Session not found: id=" + saved.getId()));
        assertThat(found.getUsers()).extracting(User::getEmail)
                .containsExactlyInAnyOrder("u1@test.com", "u2@test.com");
    }

    @Test
    @DisplayName("Suppression d'un participant d'une Session")
    void remove_participant() {
        LocalDateTime now = LocalDateTime.now();

        Teacher t = teacherRepository.save(
                Teacher.builder()
                        .firstName("T")
                        .lastName("Two")
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );

        User u = userRepository.save(User.builder()
                .firstName("U")
                .lastName("X")
                .email("u@test.com")
                .password("p")
                .createdAt(now)
                .updatedAt(now)
                .build());

        Session s = Session.builder()
                .name("Evening")
                .description("Session douce du soir")
                .date(new Date())
                .teacher(t)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();
        s.getUsers().add(u);

        s = sessionRepository.save(s);
        assertThat(s.getUsers()).hasSize(1);

        s.getUsers().remove(u);
        s = sessionRepository.save(s);

        Long sessionId = s.getId();
        Session found = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new AssertionError("Session not found: id=" + sessionId));
        assertThat(found.getUsers()).isEmpty();
    }
}
