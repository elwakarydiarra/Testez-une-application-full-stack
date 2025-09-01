package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEqualityTest {

    private static User userWithId(Long id) {
        
        return User.builder()
                .id(id)
                .email("u" + (id == null ? "n" : id) + "@test.com")
                .firstName("First")
                .lastName("Last")
                .password("secret")
                .admin(false)
                .build();
    }

    @Test
    void equals_hashcode_based_on_id_and_handle_nulls() {
        User u1a = userWithId(1L);
        User u1b = userWithId(1L);
        User u2  = userWithId(2L);
        User n1  = userWithId(null);
        User n2  = userWithId(null);

       
        assertEquals(u1a, u1b);
        assertEquals(u1a.hashCode(), u1b.hashCode());

       
        assertNotEquals(u1a, u2);

      
        assertNotEquals(u1a, n1);

        assertEquals(n1, n2);
    }
}
