package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.dto.TeacherDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherDtoEqualsBranchesTest {

    /** Sous-classe qui refuse l’égalité côté enfant pour forcer la branche false. */
    static class TeacherDtoNeverEqual extends TeacherDto {
        @Override
        protected boolean canEqual(Object other) {
            return false; // <- branch "false" couvrant la garde canEqual
        }
    }

    private static TeacherDto teacher(Long id, String fn, String ln,
                                      LocalDateTime created, LocalDateTime updated) {
        TeacherDto dto = new TeacherDto();
        dto.setId(id);
        dto.setFirstName(fn);
        dto.setLastName(ln);
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        return dto;
    }

    @Test
    void equals_hashCode_and_canEqual_branches() {
        LocalDateTime t1 = LocalDateTime.now();
        LocalDateTime t2 = t1.plusMinutes(1);

        TeacherDto a = teacher(1L, "Jane", "Doe", t1, t2);

        // self / null / autre type
        assertThat(a.equals(a)).isTrue();
        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("string")).isFalse();

        // différent
        TeacherDto b = teacher(2L, "Jane", "Doe", t1, t2);
        assertThat(a.equals(b)).isFalse();

        // identique
        TeacherDto c = teacher(1L, "Jane", "Doe", t1, t2);
        assertThat(a.equals(c)).isTrue();
        assertThat(a.hashCode()).isEqualTo(c.hashCode());

        // toString touch
        assertThat(a.toString()).contains("Jane");

        // canEqual négatif via sous-classe :
        TeacherDtoNeverEqual child = new TeacherDtoNeverEqual();
        child.setId(1L);
        child.setFirstName("Jane");
        child.setLastName("Doe");
        child.setCreatedAt(t1);
        child.setUpdatedAt(t2);

        // Parent.equals(Child) -> false (canEqual=false du child)
        assertThat(a.equals(child)).isFalse();

        // Child.equals(Parent) -> true (canEqual du parent = true ; champs identiques)
        assertThat(child.equals(a)).isTrue();
    }
}
