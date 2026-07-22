package com.viktor.booking.infrastructure.persistence.repository;

import com.viktor.booking.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository
        extends JpaRepository<UserEntity, Long> {
}
