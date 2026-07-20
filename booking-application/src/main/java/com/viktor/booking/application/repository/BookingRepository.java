package com.viktor.booking.application.repository;

import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    List<Booking> findAll();

    Optional<Booking> findById(Long id);

    List<Booking> findByStatus(BookingStatus status);

    Booking save(Booking booking);

    void deleteById(Long id);
}