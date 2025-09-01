package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTouchTest {

    @Test
    void userDto_getters_setters_equals_hash_toString() {
        UserDto u = new UserDto();
        u.setId(10L);
        u.setEmail("alice@test.com");
        u.setFirstName("Alice");
        u.setLastName("Doe");
        u.setPassword("secret");
        u.setAdmin(true);

       
        assertThat(u.getId()).isEqualTo(10L);
        assertThat(u.getEmail()).isEqualTo("alice@test.com");
        assertThat(u.getFirstName()).isEqualTo("Alice");
        assertThat(u.getLastName()).isEqualTo("Doe");
        assertThat(u.getPassword()).isEqualTo("secret");
        assertThat(u.isAdmin()).isTrue();

        
        UserDto same = new UserDto();
        same.setId(10L);
        same.setEmail("alice@test.com");
        same.setFirstName("Alice");
        same.setLastName("Doe");
        same.setPassword("secret");
        same.setAdmin(true);

        assertThat(u).isEqualTo(same);
        assertThat(u.hashCode()).isEqualTo(same.hashCode());

        same.setId(11L);
        assertThat(u).isNotEqualTo(same);

        assertThat(u.toString()).contains("alice@test.com");
    }
}
