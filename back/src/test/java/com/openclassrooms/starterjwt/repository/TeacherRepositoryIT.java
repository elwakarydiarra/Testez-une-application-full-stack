package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryIT {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void save_and_find_teacher() {
        Teacher t = Teacher.builder()
                .firstName("Albert")
                .lastName("Einstein")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher saved = teacherRepository.save(t);

        assertThat(saved.getId()).isNotNull();
        assertThat(teacherRepository.findById(saved.getId())).isPresent();
    }
}
