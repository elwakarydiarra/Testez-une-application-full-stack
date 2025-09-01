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
class PersistenceSmokeIT {

    @Autowired SessionRepository sessionRepository;
    @Autowired TeacherRepository teacherRepository;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("User, Teacher, Session: persistence + relations (teacher & participants)")
    void persist_and_link_all_together() {
        // -- 1) Teacher --
        Teacher teacher = Teacher.builder()
                .firstName("Marie")
                .lastName("Curie")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);
        assertThat(teacher.getId()).isNotNull();

       
        User user = User.builder()
                .email("participant@test.com")
                .firstName("Alice")
                .lastName("Wonder")
                .password("pwd123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        assertThat(user.getId()).isNotNull();

      
        Session session = Session.builder()
                .name("Morning Yoga")
                .description("Flow doux pour bien démarrer")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>()) 
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session.getUsers().add(user);

        session = sessionRepository.save(session);
        Long sessionId = session.getId();
        assertThat(sessionId).isNotNull();

        Session reloaded = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new AssertionError("Session not found: " + sessionId));

        assertThat(reloaded.getTeacher()).isNotNull();
        assertThat(reloaded.getTeacher().getId()).isEqualTo(teacher.getId());

        assertThat(reloaded.getUsers()).hasSize(1);
        assertThat(reloaded.getUsers().get(0).getEmail()).isEqualTo("participant@test.com");

        assertThat(reloaded.getName()).isEqualTo("Morning Yoga");
        assertThat(reloaded.getDescription()).isEqualTo("Flow doux pour bien démarrer");
        assertThat(reloaded.getDate()).isNotNull();
    }
}
