package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionParticipantsIT {

    @Autowired SessionRepository sessions;
    @Autowired TeacherRepository teachers;
    @Autowired UserRepository users;

    @Test
    void add_then_remove_participant_persistsProperly() {
        
        Teacher t = teachers.save(Teacher.builder()
                .firstName("T")
                .lastName("One")
                .build());

        User u = users.save(User.builder()
                .email("p@t.com")
                .password("pwd")
                .firstName("Pat")
                .lastName("T")
                .admin(false)
                .build());

        
        Date future = new Date(System.currentTimeMillis() + 24*60*60*1000L); 
        Session s = Session.builder()
                .name("Flow")
                .description("Morning flow")
                .date(future)
                .teacher(t)
                .users(new ArrayList<>())
                .build();

        s = sessions.saveAndFlush(s);
        Long id = s.getId();

        s.getUsers().add(u);
        sessions.saveAndFlush(s);

        Session found = sessions.findById(id).orElseThrow(() -> new IllegalStateException("Session not found"));
        assertThat(found.getUsers()).isNotNull();
        assertThat(found.getUsers()).hasSize(1);

        found.getUsers().remove(u);
        sessions.saveAndFlush(found);

        Session afterRemove = sessions.findById(id).orElseThrow(() -> new IllegalStateException("Session not found"));
        assertThat(afterRemove.getUsers()).isNotNull();
        assertThat(afterRemove.getUsers()).isEmpty();
    }
}
