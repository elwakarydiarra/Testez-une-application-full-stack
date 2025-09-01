package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toDto_maps_basic_fields() {
        LocalDateTime now = LocalDateTime.now();

        User entity = new User();
        entity.setId(11L);
        entity.setEmail("user@test.io");
        entity.setFirstName("Ada");
        entity.setLastName("Lovelace");
        entity.setPassword("hashed");
        entity.setAdmin(true);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        UserDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(11L);
        assertThat(dto.getEmail()).isEqualTo("user@test.io");
        assertThat(dto.getFirstName()).isEqualTo("Ada");
        assertThat(dto.getLastName()).isEqualTo("Lovelace");
        assertThat(dto.isAdmin()).isTrue();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toEntity_maps_basic_fields() {
        LocalDateTime t1 = LocalDateTime.now().minusDays(1);
        LocalDateTime t2 = LocalDateTime.now();

        UserDto dto = new UserDto();
        dto.setId(9L);
        dto.setEmail("map@test.io");
        dto.setFirstName("Grace");
        dto.setLastName("Hopper");
        dto.setAdmin(false);
        dto.setCreatedAt(t1);
        dto.setUpdatedAt(t2);

        User entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(9L);
        assertThat(entity.getEmail()).isEqualTo("map@test.io");
        assertThat(entity.getFirstName()).isEqualTo("Grace");
        assertThat(entity.getLastName()).isEqualTo("Hopper");
        assertThat(entity.isAdmin()).isFalse();
        assertThat(entity.getCreatedAt()).isEqualTo(t1);
        assertThat(entity.getUpdatedAt()).isEqualTo(t2);
    }

    @Test
    void null_safety() {
        assertThat(mapper.toDto((User) null)).isNull();
        assertThat(mapper.toEntity((UserDto) null)).isNull();
        assertThat(mapper.toDto((List<User>) null)).isNull();
        assertThat(mapper.toEntity((List<UserDto>) null)).isNull();
    }

    @Test
    void toDto_and_toEntity_lists() {
        LocalDateTime now = LocalDateTime.now();

        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("a@test.io");
        u1.setFirstName("A");
        u1.setLastName("AA");
        u1.setAdmin(false);
        u1.setCreatedAt(now);
        u1.setUpdatedAt(now);

        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("b@test.io");
        u2.setFirstName("B");
        u2.setLastName("BB");
        u2.setAdmin(true);
        u2.setCreatedAt(now);
        u2.setUpdatedAt(now);

        List<User> entities = Arrays.asList(u1, u2);

        List<UserDto> dtos = mapper.toDto(entities);
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getEmail()).isEqualTo("a@test.io");
        assertThat(dtos.get(1).getEmail()).isEqualTo("b@test.io");

        List<User> backEntities = mapper.toEntity(dtos);
        assertThat(backEntities).hasSize(2);
        assertThat(backEntities.get(0).getFirstName()).isEqualTo("A");
        assertThat(backEntities.get(1).getFirstName()).isEqualTo("B");
    }
}
