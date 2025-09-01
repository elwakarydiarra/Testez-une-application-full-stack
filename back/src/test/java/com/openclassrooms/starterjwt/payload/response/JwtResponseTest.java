package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void constructor_sets_fields_and_type_defaults_to_bearer() {
        JwtResponse jwt = new JwtResponse(
                "tok_123",
                42L,
                "alice@example.com", 
                "Alice",
                "Wonder",
                true
        );

        assertEquals("tok_123", jwt.getToken());
        assertEquals(42L, jwt.getId());
        assertEquals("alice@example.com", jwt.getUsername());
        assertEquals("Alice", jwt.getFirstName());
        assertEquals("Wonder", jwt.getLastName());
        assertTrue(jwt.getAdmin());

      
        assertEquals("Bearer", jwt.getType());
    }

    @Test
    void setters_update_fields_including_type() {
        JwtResponse jwt = new JwtResponse(
                "t", 1L, "u", "f", "l", false
        );

        jwt.setToken("newTok");
        jwt.setId(7L);
        jwt.setUsername("bob@yoga.com");
        jwt.setFirstName("Bob");
        jwt.setLastName("Marley");
        jwt.setAdmin(true);
        jwt.setType("Custom"); 

        assertEquals("newTok", jwt.getToken());
        assertEquals(7L, jwt.getId());
        assertEquals("bob@yoga.com", jwt.getUsername());
        assertEquals("Bob", jwt.getFirstName());
        assertEquals("Marley", jwt.getLastName());
        assertTrue(jwt.getAdmin());
        assertEquals("Custom", jwt.getType());
    }
}
