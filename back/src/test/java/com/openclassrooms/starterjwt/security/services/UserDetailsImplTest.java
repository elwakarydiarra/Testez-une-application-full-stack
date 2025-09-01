package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    @DisplayName("Builder + getters")
    void builder_and_getters() {
        UserDetailsImpl u = UserDetailsImpl.builder()
                .id(42L)
                .username("alice@example.com")
                .firstName("Alice")
                .lastName("Wonder")
                .admin(true)
                .password("secret")
                .build();

        assertEquals(42L, u.getId());
        assertEquals("alice@example.com", u.getUsername());
        assertEquals("Alice", u.getFirstName());
        assertEquals("Wonder", u.getLastName());
        assertTrue(u.getAdmin());
     
        assertEquals("secret", u.getPassword());
    }

    @Test
    @DisplayName("UserDetails flags are all true")
    void userdetails_flags_true() {
        UserDetailsImpl u = UserDetailsImpl.builder()
                .id(1L)
                .username("u")
                .password("p")
                .admin(false)
                .build();

        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isAccountNonLocked());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }

    @Test
    @DisplayName("Authorities is an empty collection")
    void authorities_empty() {
        UserDetailsImpl u = UserDetailsImpl.builder()
                .id(1L)
                .username("u")
                .password("p")
                .admin(false)
                .build();

        Collection<?> auth = u.getAuthorities();
        assertNotNull(auth);
        assertTrue(auth.isEmpty());
    }

    @Test
    @DisplayName("equals basé sur id (même id => equals true; id différent => false)")
    void equals_on_id() {
        UserDetailsImpl u1 = UserDetailsImpl.builder()
                .id(10L).username("a").password("p").admin(false).build();
        UserDetailsImpl u2 = UserDetailsImpl.builder()
                .id(10L).username("b").password("q").admin(true).build();
        UserDetailsImpl u3 = UserDetailsImpl.builder()
                .id(11L).username("c").password("r").admin(false).build();

        assertEquals(u1, u2, "Deux instances avec le même id doivent être égales");
        assertNotEquals(u1, u3, "Deux instances avec des id différents ne doivent pas être égales");
        assertNotEquals(u1, null);
        assertNotEquals(u1, new Object());
    }
}
