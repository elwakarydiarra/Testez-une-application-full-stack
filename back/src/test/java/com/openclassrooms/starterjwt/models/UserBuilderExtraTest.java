package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserBuilderExtraTest {

    @Test
    void builder_allFields_then_build_setsEverything() {
        LocalDateTime created = LocalDateTime.now().minusHours(1);
        LocalDateTime updated = LocalDateTime.now();

        User u = User.builder()
                .id(123L)
                .email("builder@test.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("p@ss")
                .admin(true)
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(u.getId()).isEqualTo(123L);
        assertThat(u.getEmail()).isEqualTo("builder@test.com");
        assertThat(u.getFirstName()).isEqualTo("Jane");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getPassword()).isEqualTo("p@ss");
        assertThat(u.isAdmin()).isTrue();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
        assertThat(u.toString()).isNotNull();
    }

    @Test
    void mutations_via_setters_cover_accessors_and_toString() {
       
        User u = new User();
        u.setEmail("partial@test.com");
        u.setFirstName("First");
        u.setLastName("Last");
        u.setPassword("pwd");
        u.setAdmin(false);

        LocalDateTime created = LocalDateTime.now().minusDays(3);
        LocalDateTime updated = LocalDateTime.now().minusDays(1);
        u.setCreatedAt(created);
        u.setUpdatedAt(updated);

        assertThat(u.getEmail()).isEqualTo("partial@test.com");
        assertThat(u.getFirstName()).isEqualTo("First");
        assertThat(u.getLastName()).isEqualTo("Last");
        assertThat(u.getPassword()).isEqualTo("pwd");
        assertThat(u.isAdmin()).isFalse();
        assertThat(u.getCreatedAt()).isEqualTo(created);
        assertThat(u.getUpdatedAt()).isEqualTo(updated);
        assertThat(u.toString()).isNotNull();
    }

    @Test
    void equals_branch_when_ids_null_then_set_same_id() {
        User u1 = new User(); 
        User u2 = new User(); 

       
        assertThat(u1).isEqualTo(u2);

        
        u2.setEmail("x@y.z");
        assertThat(u1).isEqualTo(u2); 

       
        u1.setId(1L);
        u2.setId(2L);
        assertThat(u1).isNotEqualTo(u2);

    
        u2.setId(1L);
        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

}
