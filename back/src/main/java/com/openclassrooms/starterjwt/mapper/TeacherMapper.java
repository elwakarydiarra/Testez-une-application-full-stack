package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper implements EntityMapper<TeacherDto, Teacher> {

    @Override
    public Teacher toEntity(TeacherDto dto) {
        if (dto == null) return null;

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setLastName(dto.getLastName());
        teacher.setFirstName(dto.getFirstName());
        teacher.setCreatedAt(dto.getCreatedAt());
        teacher.setUpdatedAt(dto.getUpdatedAt());
        return teacher;
    }

    @Override
    public TeacherDto toDto(Teacher teacher) {
        if (teacher == null) return null;

        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setLastName(teacher.getLastName());
        dto.setFirstName(teacher.getFirstName());
        dto.setCreatedAt(teacher.getCreatedAt());
        dto.setUpdatedAt(teacher.getUpdatedAt());
        return dto;
    }

    public List<TeacherDto> toDto(List<Teacher> teachers) {
        if (teachers == null) return null;
        return teachers.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Teacher> toEntity(List<TeacherDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
