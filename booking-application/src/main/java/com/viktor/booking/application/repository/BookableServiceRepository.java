package com.viktor.booking.application.repository;

import com.viktor.booking.domain.model.BookableService;

import java.util.List;
import java.util.Optional;

public interface BookableServiceRepository {

    BookableService save(BookableService service);

    Optional<BookableService> findById(Long id);

    List<BookableService> findAll();

    Optional<BookableService> updateActive(
            Long id,
            boolean active
    );
}
