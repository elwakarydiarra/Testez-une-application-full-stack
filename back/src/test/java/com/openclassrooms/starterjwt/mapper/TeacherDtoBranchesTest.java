package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;
import com.openclassrooms.starterjwt.dto.TeacherDto;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherDtoBranchesTest {

    @Test
    void equals_hash_ok_same_fields() {
        TeacherDto a = new TeacherDto();
        a.setId(10L);
        a.setFirstName("John");
        a.setLastName("Doe");

        TeacherDto b = new TeacherDto();
        b.setId(10L);
        b.setFirstName("John");
        b.setLastName("Doe");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("John").contains("Doe");
    }

    @Test
    void equals_false_when_lastName_differs() {
        TeacherDto a = new TeacherDto(); a.setId(1L); a.setLastName("A");
        TeacherDto b = new TeacherDto(); b.setId(1L); b.setLastName("B");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equals_false_with_null_and_other_class() {
        TeacherDto t = new TeacherDto();
        t.setId(1L);

        assertThat(t.equals(null)).isFalse();            
        assertThat(t.equals(new Object())).isFalse();    
    }

    @Test
    void equals_true_when_both_firstName_null_and_self_equality_then_false_when_one_side_null() {
        TeacherDto a = new TeacherDto(); a.setId(2L); a.setFirstName(null); a.setLastName("Z");
        TeacherDto b = new TeacherDto(); b.setId(2L); b.setFirstName(null); b.setLastName("Z");

        assertThat(a).isEqualTo(b); 
        assertThat(a).isEqualTo(a); 
        
        b.setFirstName("John");     
        assertThat(a).isNotEqualTo(b);
    }
}
