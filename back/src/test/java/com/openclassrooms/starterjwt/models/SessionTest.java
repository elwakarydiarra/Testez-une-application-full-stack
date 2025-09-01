package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void testUsersList() {
        User u1 = new User();
        u1.setId(1L);
        u1.setFirstName("Alice");

        User u2 = new User();
        u2.setId(2L);
        u2.setFirstName("Bob");

        List<User> users = Stream.of(u1, u2).collect(Collectors.toList());

        Session session = new Session();
        session.setUsers(users);

        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(u1));
        assertTrue(session.getUsers().contains(u2));
    }
}
