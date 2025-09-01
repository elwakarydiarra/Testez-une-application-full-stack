package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock SessionRepository sessionRepository;
    @Mock UserRepository userRepository;

    @InjectMocks SessionService service;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<User>());

        user = new User();
        user.setId(10L);
    }

    @Test
    void create_ok() {
        when(sessionRepository.save(session)).thenReturn(session);
        Session saved = service.create(session);
        assertThat(saved).isSameAs(session);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_ok() {
        service.delete(1L);
        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_ok() {
        List<Session> list = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(list);

        List<Session> result = service.findAll();
        assertThat(result).containsExactly(session);
    }

    @Test
    void getById_found_returnsSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Session result = service.getById(1L);
        assertThat(result).isSameAs(session);
    }

    @Test
    void getById_notFound_returnsNull() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());
        Session result = service.getById(99L);
        assertThat(result).isNull();
    }

    @Test
    void update_setsId_andSaves() {
        Session input = new Session();
        input.setUsers(new ArrayList<User>());
        when(sessionRepository.save(any(Session.class))).thenReturn(input);

        Session result = service.update(5L, input);

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        Session saved = captor.getValue();

        assertThat(saved.getId()).isEqualTo(5L);
        assertThat(result).isSameAs(input);
    }

    // --- participate ---

    @Test
    void participate_ok() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        service.participate(1L, 10L);

        assertThat(session.getUsers()).containsExactly(user);
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_sessionOrUser_notFound_throws() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class, () -> service.participate(1L, 10L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void participate_alreadyParticipating_throwsBadRequest() {
        session.setUsers(new ArrayList<User>(Collections.singletonList(user)));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> service.participate(1L, 10L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // --- noLongerParticipate ---

    @Test
    void noLongerParticipate_ok() {
        session.setUsers(new ArrayList<User>(Collections.singletonList(user)));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        service.noLongerParticipate(1L, 10L);

        assertThat(session.getUsers()).isEmpty();
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_sessionNotFound_throws() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.noLongerParticipate(1L, 10L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void noLongerParticipate_userNotInSession_throwsBadRequest() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> service.noLongerParticipate(1L, 10L));
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
