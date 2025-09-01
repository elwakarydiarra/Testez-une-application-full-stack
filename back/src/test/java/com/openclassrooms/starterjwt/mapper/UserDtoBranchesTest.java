package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;


class UserDtoBranchesTest {

    @Test
    void equals_hashCode_true_when_same_fields() {
        UserDto a = new UserDto();
        a.setId(1L);
        a.setEmail("a@a.com");
        a.setFirstName("Alice");
        a.setLastName("Wonder");
        a.setAdmin(false);
        a.setPassword("x");

        UserDto b = new UserDto();
        b.setId(1L);
        b.setEmail("a@a.com");
        b.setFirstName("Alice");
        b.setLastName("Wonder");
        b.setAdmin(false);
        b.setPassword("x");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("Alice").contains("a@a.com");
    }

    @Test
    void equals_false_when_one_field_differs() {
        UserDto a = new UserDto();
        a.setId(1L);

        UserDto b = new UserDto();
        b.setId(2L); 
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equals_false_with_null_and_other_class() {
        UserDto u = new UserDto();
        u.setId(1L);

        assertThat(u.equals(null)).isFalse();             
        assertThat(u.equals("not a UserDto")).isFalse();  
    }

    @Test
    void equals_true_when_both_password_null_then_false_when_one_side_null_and_self_equality() {
        UserDto a = new UserDto();
        a.setId(3L);
        a.setEmail("x@y.z");
        a.setPassword(null);

        UserDto b = new UserDto();
        b.setId(3L);
        b.setEmail("x@y.z");
        b.setPassword(null);

        assertThat(a).isEqualTo(b);   
        assertThat(a).isEqualTo(a);   

        b.setPassword("secret");     
        assertThat(a).isNotEqualTo(b);
    }
}
