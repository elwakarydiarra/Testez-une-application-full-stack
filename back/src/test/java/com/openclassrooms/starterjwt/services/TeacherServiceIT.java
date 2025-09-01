package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TeacherServiceIT {

    @Autowired private TeacherService teacherService;
    @Autowired private TeacherRepository teacherRepository;

    @BeforeEach
    void setup() {
        teacherRepository.deleteAll();
    }

    @Test
    @DisplayName("findById retrouve un teacher persisté")
    void findById_returnsPersisted() {
        Teacher saved = teacherRepository.save(
                Teacher.builder()
                        .firstName("Grace")
                        .lastName("Hopper")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        Teacher found = teacherService.findById(saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getLastName()).isEqualTo("Hopper");
    }

    @Test
    @DisplayName("findAll retourne les teachers")
    void findAll_returnsItems() {
        teacherRepository.save(Teacher.builder().firstName("A").lastName("A").build());
        teacherRepository.save(Teacher.builder().firstName("B").lastName("B").build());

        List<Teacher> all = teacherService.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }
}
