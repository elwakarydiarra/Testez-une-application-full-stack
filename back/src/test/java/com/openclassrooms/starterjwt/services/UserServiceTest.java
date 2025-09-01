package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_returnsUser_whenPresent() {
        User u = User.builder()
                .id(1L)
                .email("jane@yoga.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("secret")
                .admin(false)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User out = userService.findById(1L);

        assertThat(out).isNotNull();
        assertThat(out.getEmail()).isEqualTo("jane@yoga.com");
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_returnsNull_whenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User out = userService.findById(99L);

        assertThat(out).isNull();
        verify(userRepository).findById(99L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void delete_delegatesToRepository() {
        userService.delete(5L);

        verify(userRepository).deleteById(5L);
        verifyNoMoreInteractions(userRepository);
    }
}
