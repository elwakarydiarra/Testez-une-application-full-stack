package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplPojoTest {

    @Test
    void builder_and_toString_shouldWork() {
        UserDetailsImpl u = UserDetailsImpl.builder()
                .id(42L)
                .username("alice@test.com")
                .firstName("Alice")
                .lastName("Doe")
                .admin(true)
                .password("secret")
                .build();

        
        assertThat(u.getId()).isEqualTo(42L);
        assertThat(u.getUsername()).isEqualTo("alice@test.com");
        assertThat(u.getFirstName()).isEqualTo("Alice");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getAdmin()).isTrue();
        assertThat(u.getPassword()).isEqualTo("secret");

       
        assertThat(u.toString()).isNotNull();
    }

    @Test
    void equals_depends_on_id_and_hashCode_is_stable_per_instance() {
        UserDetailsImpl u1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl u2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl u3 = UserDetailsImpl.builder().id(2L).build();

       
        assertThat(u1).isEqualTo(u2);
        assertThat(u1).isNotEqualTo(u3);

       
        int h1 = u1.hashCode();
        int h2 = u1.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void equals_isFalse_with_null_or_other_class() {
        UserDetailsImpl u = UserDetailsImpl.builder().id(1L).build();
        assertThat(u.equals(null)).isFalse();
        assertThat(u.equals("not-a-user")).isFalse();
    }
}
