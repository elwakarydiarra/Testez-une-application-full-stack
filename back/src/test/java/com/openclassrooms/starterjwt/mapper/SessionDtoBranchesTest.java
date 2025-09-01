package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.dto.SessionDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDtoBranchesTest {

    @Test
    void equals_hash_true_when_all_fields_equal() {
        LocalDateTime fixed = LocalDateTime.of(2024, 1, 1, 0, 0, 0); 

        SessionDto a = new SessionDto();
        a.setId(42L);
        a.setName("Flow");
        a.setDescription("Desc");
        a.setTeacher_id(100L);
        a.setUsers(Arrays.asList(1L, 2L));
        a.setCreatedAt(fixed);
        a.setUpdatedAt(fixed);

        SessionDto b = new SessionDto();
        b.setId(42L);
        b.setName("Flow");
        b.setDescription("Desc");
        b.setTeacher_id(100L);
        b.setUsers(Arrays.asList(1L, 2L));
        b.setCreatedAt(fixed);
        b.setUpdatedAt(fixed);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("Flow");
    }

    @Test
    void equals_false_on_users_null_vs_empty_and_on_name_diff() {
        SessionDto a = new SessionDto();
        a.setId(1L);
        a.setUsers(null);

        SessionDto b = new SessionDto();
        b.setId(1L);
        b.setUsers(Collections.emptyList());   
        assertThat(a).isNotEqualTo(b);

        b.setUsers(null);
        a.setName("A");
        b.setName("B");                        
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equals_false_with_null_and_other_class() {
        SessionDto s = new SessionDto();
        s.setId(1L);

        assertThat(s.equals(null)).isFalse();               
        assertThat(s.equals("not a SessionDto")).isFalse(); 
    }

    @Test
    void equals_true_when_both_null_fields_match_and_self_equality() {
        
        SessionDto s = new SessionDto();
        s.setId(2L);
       
        assertThat(s).isEqualTo(s); 

        SessionDto t = new SessionDto();
        t.setId(2L);
        assertThat(s).isEqualTo(t); 
    }

    @Test
    void equals_false_when_teacher_id_differs_and_null_vs_value() {
        SessionDto a = new SessionDto();
        a.setId(3L);
        a.setTeacher_id(10L);
        a.setName(null);

        SessionDto b = new SessionDto();
        b.setId(3L);
        b.setTeacher_id(11L);  
        b.setName(null);

        assertThat(a).isNotEqualTo(b);

       
        b.setTeacher_id(10L);
        b.setName("X");
        assertThat(a).isNotEqualTo(b);
    }
}
