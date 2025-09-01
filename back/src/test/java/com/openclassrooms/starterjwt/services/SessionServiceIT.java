package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class SessionServiceIT {

    @Autowired private SessionService sessionService;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();

        teacher = teacherRepository.save(
                Teacher.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }

    private Session newSession(String name) {
        return Session.builder()
                .name(name)
                .description("Desc " + name)
                .date(new Date(System.currentTimeMillis() + 86_400_000)) 
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("getById retrouve la session persistée")
    void getById_returnsPersisted() {
        Session saved = sessionRepository.save(newSession("Morning"));
        Session found = sessionService.getById(saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Morning");
        assertThat(found.getTeacher().getId()).isEqualTo(teacher.getId());
    }

    @Test
    @DisplayName("findAll retourne les sessions")
    void findAll_returnsItems() {
        sessionRepository.save(newSession("A"));
        sessionRepository.save(newSession("B"));

        List<Session> all = sessionService.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("delete supprime la session")
    void delete_removes() {
        Session saved = sessionRepository.save(newSession("ToDelete"));
        Long id = saved.getId();

        sessionService.delete(id);

        assertThat(sessionRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("create puis update modifient correctement les champs")
    void create_then_update_roundtrip() {
        
        Session created = sessionRepository.save(newSession("Initial"));
        Long id = created.getId();
        assertThat(id).isNotNull();

        
        Session changes = new Session();
        changes.setName("Initial (updated)");
        changes.setDescription("New description");
        changes.setDate(new Date(System.currentTimeMillis() + 2 * 86_400_000)); 
        changes.setTeacher(teacher); 

       
        sessionService.update(id, changes);

        Session after = sessionService.getById(id);
        assertThat(after).isNotNull();
        assertThat(after.getName()).isEqualTo("Initial (updated)");
        assertThat(after.getDescription()).isEqualTo("New description");
        assertThat(after.getDate()).isNotNull();
        assertThat(after.getTeacher().getId()).isEqualTo(teacher.getId());
    }

    @Test
    @DisplayName("getById retourne null pour un ID inconnu et delete lève EmptyResultDataAccessException")
    void getById_unknown_returnsNull_and_delete_unknown_throws() {
        Long unknown = 999_999L;

        
        assertThat(sessionService.getById(unknown)).isNull();

        
        assertThatThrownBy(() -> sessionService.delete(unknown))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}