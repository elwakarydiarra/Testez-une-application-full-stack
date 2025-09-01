package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DtoBranchEdgeCasesTest {

   
    @Test
    @DisplayName("UserDto: equals/hashCode – tous les chemins (self, null, autre classe, id null/non-null, id égal/différent)")
    void userDto_equals_hashcode_all_branches() {
        UserDto a = new UserDto();
        a.setId(null); // id null
        a.setEmail("a@test.com");
        a.setFirstName("A");
        a.setLastName("A");
        a.setAdmin(false);
        a.setPassword("x");

        
        assertThat(a.equals(a)).isTrue();

       
        assertThat(a.equals(null)).isFalse();

        
        assertThat(a.equals("not-a-user")).isFalse();

       
        UserDto a2 = new UserDto();
        a2.setId(null);
        a2.setEmail("a@test.com");
        a2.setFirstName("A");
        a2.setLastName("A");
        a2.setAdmin(false);
        a2.setPassword("x");
        assertThat(a).isEqualTo(a2);
        assertThat(a.hashCode()).isEqualTo(a2.hashCode()); 
        
        a.setId(10L);
        a2.setId(10L);
        assertThat(a).isEqualTo(a2);
        assertThat(a.hashCode()).isEqualTo(a2.hashCode());

        
        a2.setId(11L);
        assertThat(a.equals(a2)).isFalse();

        
        assertThat(a.toString()).contains("10");
    }

    
    @Test
    @DisplayName("TeacherDto: equals/hashCode – mêmes branches que UserDto")
    void teacherDto_equals_hashcode_all_branches() {
        TeacherDto t1 = new TeacherDto();
        t1.setId(null);
        t1.setFirstName("Jane");
        t1.setLastName("Guru");

        
        assertThat(t1.equals(t1)).isTrue();
        assertThat(t1.equals(null)).isFalse();
        assertThat(t1.equals(123)).isFalse();

        TeacherDto t2 = new TeacherDto();
        t2.setId(null);
        t2.setFirstName("Jane");
        t2.setLastName("Guru");

        
        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());

        
        t1.setId(7L);
        t2.setId(7L);
        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());

        
        t2.setId(8L);
        assertThat(t1.equals(t2)).isFalse();

        assertThat(t1.toString()).contains("Jane");
    }

    
    @Test
    @DisplayName("SessionDto: equals/hashCode – chemins id null/non-null + users null/vide/non-vide")
    void sessionDto_equals_hashcode_users_branches() {
        Date now = new Date();

        SessionDto s1 = new SessionDto();
        s1.setId(null);
        s1.setName("Morning");
        s1.setDescription("Desc");
        s1.setDate(now);
        s1.setTeacher_id(99L);
        

       
        assertThat(s1.equals(s1)).isTrue();
        assertThat(s1.equals(null)).isFalse();
        assertThat(s1.equals(now)).isFalse();

        
        SessionDto s2 = new SessionDto();
        s2.setId(null);
        s2.setName("Morning");
        s2.setDescription("Desc");
        s2.setDate(now);
        s2.setTeacher_id(99L);
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode()); 

        
        s1.setUsers(new ArrayList<>());
        assertThat(s1.equals(s2)).isFalse();

        
        s2.setUsers(new ArrayList<>());
        assertThat(s1).isEqualTo(s2);

        
        s1.getUsers().add(10L);
        assertThat(s1.equals(s2)).isFalse();

        
        s1.setId(42L);
        s2.setId(42L);
        s2.getUsers().add(10L);
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());

        
        s2.setId(43L);
        assertThat(s1.equals(s2)).isFalse();

        assertThat(s1.toString()).contains("Morning");
    }
    @Test
    void userDto_canEqual_and_diff_fields() {
        UserDto u1 = new UserDto();
        UserDto u2 = new UserDto();
        u1.setId(null);
        u2.setId(null);
        u1.setEmail("a@test.com");
        u2.setEmail("b@test.com");

        
        assertThat(u1.equals(u2)).isFalse();

        
        TeacherDto t = new TeacherDto();
        assertThat(u1.equals(t)).isFalse();
    }

    @Test
    void sessionDto_diff_teacherId_and_date() {
        SessionDto s1 = new SessionDto();
        s1.setId(1L);
        s1.setName("Yoga");
        s1.setTeacher_id(100L);

        SessionDto s2 = new SessionDto();
        s2.setId(1L);
        s2.setName("Yoga");
        s2.setTeacher_id(200L);

        
        assertThat(s1.equals(s2)).isFalse();

       
        s1.setDate(new java.util.Date(1000));
        s2.setDate(new java.util.Date(2000));
        assertThat(s1.equals(s2)).isFalse();
    }

    @Test
    void teacherDto_canEqual_and_diff_fields() {
        TeacherDto t1 = new TeacherDto();
        TeacherDto t2 = new TeacherDto();
        t1.setId(null);
        t2.setId(null);
        t1.setFirstName("A");
        t2.setFirstName("B");

        
        assertThat(t1.equals(t2)).isFalse();

        
        UserDto u = new UserDto();
        assertThat(t1.equals(u)).isFalse();
    }

}
