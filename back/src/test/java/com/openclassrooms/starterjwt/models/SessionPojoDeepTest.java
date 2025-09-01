package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class SessionPojoDeepTest {

    @Test
    void setters_getters_dates_teacher_users_and_toString() {
        Session s = new Session();

        
        s.setId(1L);
        s.setName("Morning Yoga");
        s.setDescription("Sun salutations");

        
        Date d = new Date(); 
        LocalDateTime created = LocalDateTime.now().minusHours(1);
        LocalDateTime updated = LocalDateTime.now().plusHours(1);

        s.setDate(d);
        s.setCreatedAt(created);
        s.setUpdatedAt(updated);

       
        Teacher t = new Teacher();
        t.setId(99L);
        t.setLastName("Guru");
        t.setFirstName("Yogi");
        s.setTeacher(t);

       
        s.setUsers(new ArrayList<>());
        User u1 = User.builder()
                .id(10L).email("a@a.com").firstName("A").lastName("A").password("x").admin(false)
                .build();
        User u2 = User.builder()
                .id(11L).email("b@b.com").firstName("B").lastName("B").password("y").admin(false)
                .build();

        s.getUsers().add(u1);
        s.getUsers().add(u2);
        assertThat(s.getUsers()).hasSize(2).contains(u1, u2);

        s.getUsers().remove(u1);
        assertThat(s.getUsers()).hasSize(1).containsExactly(u2);

       
        assertThat(s.getId()).isEqualTo(1L);
        assertThat(s.getName()).isEqualTo("Morning Yoga");
        assertThat(s.getDescription()).isEqualTo("Sun salutations");
        assertThat(s.getDate()).isEqualTo(d);
        assertThat(s.getCreatedAt()).isEqualTo(created);
        assertThat(s.getUpdatedAt()).isEqualTo(updated);
        assertThat(s.getTeacher().getId()).isEqualTo(99L);

      
        String ts = s.toString();
        assertThat(ts).isNotNull();
        assertThat(ts).contains("Morning Yoga");
        assertThat(ts).contains("Sun salutations");
    }

    @Test
    void equals_and_hashCode_use_id_when_present() {
        Session a = new Session();
        Session b = new Session();
        a.setId(7L);
        b.setId(7L);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        b.setId(8L);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void hashCode_is_stable_when_id_is_null_and_toString_not_null() {
        Session s = new Session();
        int h1 = s.hashCode();
        int h2 = s.hashCode();
        assertThat(h1).isEqualTo(h2);

        assertThat(s.toString()).isNotNull();
    }
}
