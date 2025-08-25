package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SessionMapper implements EntityMapper<SessionDto, Session> {

    private final TeacherService teacherService;
    private final UserService userService;

    public SessionMapper(TeacherService teacherService, UserService userService) {
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @Override
    public Session toEntity(SessionDto dto) {
        if (dto == null) return null;

        Session session = new Session();
        session.setId(dto.getId());
        session.setName(dto.getName());
        session.setDate(dto.getDate());
        session.setDescription(dto.getDescription());
        session.setCreatedAt(dto.getCreatedAt());
        session.setUpdatedAt(dto.getUpdatedAt());

        // teacher
        if (dto.getTeacher_id() != null) {
            Teacher teacher = teacherService.findById(dto.getTeacher_id());
            session.setTeacher(teacher);
        }

        // users
        if (dto.getUsers() != null) {
            List<User> users = dto.getUsers().stream()
                    .map(userService::findById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            session.setUsers(users);
        } else {
            session.setUsers(Collections.emptyList());
        }

        return session;
    }

    @Override
    public SessionDto toDto(Session session) {
        if (session == null) return null;

        SessionDto dto = new SessionDto();
        dto.setId(session.getId());
        dto.setName(session.getName());
        dto.setDate(session.getDate());
        dto.setDescription(session.getDescription());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());

        // teacher
        dto.setTeacher_id(session.getTeacher() != null ? session.getTeacher().getId() : null);

        // users
        if (session.getUsers() != null) {
            dto.setUsers(session.getUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        } else {
            dto.setUsers(Collections.emptyList());
        }

        return dto;
    }

    // mapping de listes si besoin
    public List<SessionDto> toDto(List<Session> sessions) {
        if (sessions == null) return null;
        return sessions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Session> toEntity(List<SessionDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
