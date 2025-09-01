package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionRepositoryIT {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setup() {
        teacher = Teacher.builder()
                .firstName("Marie")
                .lastName("Curie")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);
    }

    private Session newSession(String name) {
        return Session.builder()
                .name(name)
                .description("Desc " + name)
                .date(new Date(System.currentTimeMillis() + 86_400_000L)) 
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save_and_findById() {
        Session saved = sessionRepository.save(newSession("Yoga Doux"));
        Optional<Session> found = sessionRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Yoga Doux");
        assertThat(found.get().getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(found.get().getDate()).isNotNull();
    }

    @Test
    void findAll_returnsItems() {
        sessionRepository.save(newSession("Vinyasa"));
        sessionRepository.save(newSession("Ashtanga"));

        List<Session> all = sessionRepository.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void deleteById_removesEntity() {
        Session saved = sessionRepository.save(newSession("Hatha"));
        Long id = saved.getId();

        sessionRepository.deleteById(id);

        assertThat(sessionRepository.findById(id)).isEmpty();
    }
}
