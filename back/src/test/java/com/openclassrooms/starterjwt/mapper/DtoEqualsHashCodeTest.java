package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DtoEqualsHashCodeTest {

    @Test
    void userDto_equals_and_hashCode_branches() {
        UserDto u1 = new UserDto();
        u1.setId(1L);
        UserDto u2 = new UserDto();
        u2.setId(1L);

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());

        UserDto u3 = new UserDto();
        u3.setId(2L);
        assertThat(u1).isNotEqualTo(u3);
        assertThat(u1.equals(null)).isFalse();
        assertThat(u1.equals("string")).isFalse();
    }

    @Test
    void sessionDto_equals_and_hashCode_branches() {
        Date now = new Date();
        SessionDto s1 = new SessionDto();
        s1.setId(1L);
        s1.setName("Yoga");
        s1.setDate(now);

        SessionDto s2 = new SessionDto();
        s2.setId(1L);
        s2.setName("Yoga");
        s2.setDate(now);

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());

        SessionDto s3 = new SessionDto();
        s3.setId(2L);
        assertThat(s1).isNotEqualTo(s3);
        assertThat(s1.equals(null)).isFalse();
        assertThat(s1.equals("string")).isFalse();
    }

    @Test
    void teacherDto_equals_and_hashCode_branches() {
        TeacherDto t1 = new TeacherDto();
        t1.setId(1L);
        t1.setFirstName("Jane");

        TeacherDto t2 = new TeacherDto();
        t2.setId(1L);
        t2.setFirstName("Jane");

        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());

        TeacherDto t3 = new TeacherDto();
        t3.setId(2L);
        assertThat(t1).isNotEqualTo(t3);
        assertThat(t1.equals(null)).isFalse();
        assertThat(t1.equals("string")).isFalse();
    }
}
