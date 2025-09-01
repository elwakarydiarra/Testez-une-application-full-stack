package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.dto.SessionDto;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDtoEqualsBranchesTest {

    /** Sous-classe qui force le refus canEqual côté enfant pour déclencher la branche false. */
    static class SessionDtoNeverEqual extends SessionDto {
        @Override
        protected boolean canEqual(Object other) {
            return false; // <- clé : branch "false" dans le parent.equals(child)
        }
    }

    private static SessionDto session(
            Long id, String name, String desc, Date date, Long teacherId, Long... users) {
        SessionDto dto = new SessionDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setDate(date);
        dto.setTeacher_id(teacherId);
        dto.setUsers(Arrays.asList(users));
        return dto;
    }

    @Test
    void equals_hashCode_and_canEqual_branches() {
        Date now = new Date();

        SessionDto s1 = session(1L, "Morning", "Desc", now, 10L, 1L, 2L);

        // self / null / autre type
        assertThat(s1.equals(s1)).isTrue();
        assertThat(s1.equals(null)).isFalse();
        assertThat(s1.equals("not a dto")).isFalse();

        // différent
        SessionDto s2 = session(2L, "Morning", "Desc", now, 10L, 1L, 2L);
        assertThat(s1.equals(s2)).isFalse();

        // identique
        SessionDto s3 = session(1L, "Morning", "Desc", now, 10L, 1L, 2L);
        assertThat(s1.equals(s3)).isTrue();
        assertThat(s1.hashCode()).isEqualTo(s3.hashCode());

        // toString : "touch"
        assertThat(s1.toString()).contains("Morning");

        // canEqual négatif via sous-classe :
        SessionDtoNeverEqual child = new SessionDtoNeverEqual();
        child.setId(1L);
        child.setName("Morning");
        child.setDescription("Desc");
        child.setDate(now);
        child.setTeacher_id(10L);
        child.setUsers(Arrays.asList(1L, 2L));

        // Parent.equals(Child) -> false (branche canEqual=false dans le parent)
        assertThat(s1.equals(child)).isFalse();

        // Child.equals(Parent) -> true (le parent a canEqual=true ; champs identiques)
        assertThat(child.equals(s1)).isTrue();
    }
}
