package com.viktor.booking.infrastructure.persistence.repository;

import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookableServiceJpaRepository
        extends JpaRepository<BookableServiceEntity, Long> {
}
