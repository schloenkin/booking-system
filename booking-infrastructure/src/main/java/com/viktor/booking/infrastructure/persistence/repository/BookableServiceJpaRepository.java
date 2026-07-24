package com.viktor.booking.infrastructure.persistence.repository;

import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BookableServiceJpaRepository
        extends JpaRepository<BookableServiceEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select service
        from BookableServiceEntity service
        where service.id = :id
        """)
    Optional<BookableServiceEntity> findByIdForUpdate(
            @Param("id") Long id
    );
}
