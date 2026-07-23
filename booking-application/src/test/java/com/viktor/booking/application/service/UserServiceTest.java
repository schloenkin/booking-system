package com.viktor.booking.application.service;

import com.viktor.booking.application.repository.UserRepository;
import com.viktor.booking.domain.enums.UserRole;
import com.viktor.booking.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.viktor.booking.application.security.PasswordHasher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithUserRole() {
        when(passwordHasher.hash("raw-password"))
                .thenReturn("hashed-password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);

                    return new User(
                            1L,
                            user.getEmail(),
                            user.getPasswordHash(),
                            user.getRole()
                    );
                });

        User createdUser = userService.createUser(
                "user@example.com",
                "raw-password"
        );

        verify(passwordHasher)
                .hash("raw-password");

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        User userPassedToRepository =
                userCaptor.getValue();

        assertThat(userPassedToRepository.getId())
                .isNull();

        assertThat(userPassedToRepository.getEmail())
                .isEqualTo("user@example.com");

        assertThat(userPassedToRepository.getPasswordHash())
                .isEqualTo("hashed-password");

        assertThat(userPassedToRepository.getRole())
                .isEqualTo(UserRole.USER);

        assertThat(createdUser.getId())
                .isEqualTo(1L);
    }

    @Test
    void shouldReturnUserById() {
        User user = new User(
                1L,
                "existing-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Optional<User> result =
                userService.getUserById(1L);

        assertThat(result)
                .containsSame(user);

        verify(userRepository)
                .findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenUserDoesNotExist() {
        when(userRepository.findById(999999L))
                .thenReturn(Optional.empty());

        Optional<User> result =
                userService.getUserById(999999L);

        assertThat(result)
                .isEmpty();

        verify(userRepository)
                .findById(999999L);
    }
}