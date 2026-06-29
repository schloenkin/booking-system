package com.viktor.booking.repository;

import com.viktor.booking.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    List<Booking> findAll();

    Optional<Booking> findById(Long id);

    Booking save(Booking booking);

    void deleteById(Long id);
}