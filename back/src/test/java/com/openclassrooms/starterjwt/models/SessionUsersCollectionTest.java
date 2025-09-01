package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class SessionUsersCollectionTest {

    private User user(long id) {
        return User.builder()
                .id(id)
                .email("u"+id+"@test.com")
                .lastName("L"+id)
                .firstName("F"+id)
                .password("pwd")
                .admin(false)
                .build();
    }

    @Test
    void add_and_remove_users_from_session_list() {
        Session s = new Session();
        s.setName("Yoga");
        s.setDescription("Desc");
        s.setUsers(new ArrayList<>());

        User u1 = user(1L);
        User u2 = user(2L);

        s.getUsers().add(u1);
        s.getUsers().add(u2);

        assertThat(s.getUsers()).hasSize(2).contains(u1, u2);

        s.getUsers().remove(u1);
        assertThat(s.getUsers()).hasSize(1).containsExactly(u2);
    }
}
