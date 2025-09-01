package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoEqualsBranchesTest {

    
    static class UserDtoNeverEqual extends UserDto {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    private static UserDto user(Long id, String email, String fn, String ln, Boolean admin) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setFirstName(fn);
        dto.setLastName(ln);
        dto.setAdmin(admin);
        return dto;
    }

    @Test
    void equals_hashCode_and_canEqual_branches() {
        UserDto a = user(1L, "a@a.a", "Alice", "Wonder", true);

        
        assertThat(a.equals(a)).isTrue();
        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("x")).isFalse();

        
        UserDto b = user(2L, "a@a.a", "Alice", "Wonder", true);
        assertThat(a.equals(b)).isFalse();

        
        UserDto c = user(1L, "a@a.a", "Alice", "Wonder", true);
        assertThat(a.equals(c)).isTrue();
        assertThat(a.hashCode()).isEqualTo(c.hashCode());

        
        assertThat(a.toString()).contains("Alice");

        
        UserDtoNeverEqual child = new UserDtoNeverEqual();
        child.setId(1L);
        child.setEmail("a@a.a");
        child.setFirstName("Alice");
        child.setLastName("Wonder");
        child.setAdmin(true);

        
        assertThat(a.equals(child)).isFalse();
        
        assertThat(child.equals(a)).isTrue();
    }
}
