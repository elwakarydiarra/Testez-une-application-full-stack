package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TeacherEntityIT {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Persist & find Teacher")
    void persist_and_find_teacher() {
        LocalDateTime now = LocalDateTime.now();

        Teacher t = Teacher.builder()
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher saved = teacherRepository.save(t);
        assertThat(saved.getId()).isNotNull();

        Teacher found = teacherRepository.findById(saved.getId())
                .orElseThrow(() -> new AssertionError("Teacher not found: id=" + saved.getId())); 
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
    }
}
