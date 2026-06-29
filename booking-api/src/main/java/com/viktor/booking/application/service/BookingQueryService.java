package com.viktor.booking.application.service;

import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryService {

    public List<Booking> getAllBookings() {
        Booking booking1 = new Booking(
                1L,
                1L,
                1L,
                LocalDateTime.of(2026, 6, 25, 10, 0),
                LocalDateTime.of(2026, 6, 25, 10, 30),
                BookingStatus.CONFIRMED
        );


        Booking booking2 = new Booking(
                2L,
                2L,
                2L,
                LocalDateTime.of(2026, 6, 25, 11, 0),
                LocalDateTime.of(2026, 6, 25, 12, 0),
                BookingStatus.PENDING
        );

        return List.of(booking1, booking2);
    }

    public Optional<Booking> getBookingById(Long id) {
        return getAllBookings()
                .stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }

}