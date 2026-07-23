package com.viktor.booking.application.repository;

import com.viktor.booking.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    User save(User user);
}
