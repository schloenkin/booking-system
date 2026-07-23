package com.viktor.booking.application.service;

import com.viktor.booking.application.repository.UserRepository;
import com.viktor.booking.domain.enums.UserRole;
import com.viktor.booking.domain.model.User;
import org.springframework.stereotype.Service;
import com.viktor.booking.application.security.PasswordHasher;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(
            String email,
            String rawPassword
    ) {
        String passwordHash =
                passwordHasher.hash(rawPassword);

        User user = new User(
                null,
                email,
                passwordHash,
                UserRole.USER
        );

        return userRepository.save(user);
    }
}
