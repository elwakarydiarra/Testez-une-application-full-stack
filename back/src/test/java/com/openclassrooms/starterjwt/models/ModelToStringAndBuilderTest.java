package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ModelToStringAndBuilderTest {

    @Test
    void user_builder_chain_and_toString() {
        LocalDateTime now = LocalDateTime.now();
        User u = User.builder()
                .id(1L).email("a@b.cd").firstName("A").lastName("B")
                .password("p").admin(false).build();
        u.setCreatedAt(now).setUpdatedAt(now);

        assertThat(u.getEmail()).isEqualTo("a@b.cd");
        assertThat(u.toString()).contains("User");
    }

    @Test
    void teacher_builder_and_toString() {
        Teacher t = Teacher.builder().id(5L).firstName("T").lastName("E").build();
        assertThat(t.getFirstName()).isEqualTo("T");
        assertThat(t.toString()).contains("Teacher");
    }

    @Test
    void session_builder_and_toString() {
        Session s = Session.builder()
                .id(9L).name("S").description("D").date(new Date()).build();
        assertThat(s.getName()).isEqualTo("S");
        assertThat(s.toString()).contains("Session");
    }
}
