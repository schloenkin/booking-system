package com.viktor.booking.infrastructure.persistence.repository;

import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.infrastructure.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingJpaRepository
        extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByStatus(BookingStatus status);
}
