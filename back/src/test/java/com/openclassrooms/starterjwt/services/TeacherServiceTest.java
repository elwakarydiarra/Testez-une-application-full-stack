package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    @DisplayName("findById retourne le teacher quand il existe")
    void findById_found() {
        Teacher t = Teacher.builder().id(1L).firstName("Ada").lastName("Lovelace").build();
        given(teacherRepository.findById(1L)).willReturn(Optional.of(t));

        Teacher result = teacherService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Ada");
    }

    @Test
    @DisplayName("findById retourne null quand inexistant")
    void findById_notFound() {
        given(teacherRepository.findById(99L)).willReturn(Optional.empty());

        Teacher result = teacherService.findById(99L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("findAll retourne la liste depuis le repository")
    void findAll_returnsList() {
        List<Teacher> data = Arrays.asList(
                Teacher.builder().id(1L).firstName("A").lastName("A").build(),
                Teacher.builder().id(2L).firstName("B").lastName("B").build()
        );
        given(teacherRepository.findAll()).willReturn(data);

        List<Teacher> all = teacherService.findAll();

        assertThat(all).hasSize(2);
    }
}
