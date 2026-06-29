package com.viktor.booking.application.service;

import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryService {

    private final List<Booking> bookings = new ArrayList<>();
    private Long nextId = 3L;

    public BookingQueryService() {
        bookings.add(new Booking(
                1L,
                1L,
                1L,
                LocalDateTime.of(2026, 6, 25, 10, 0),
                LocalDateTime.of(2026, 6, 25, 10, 30),
                BookingStatus.CONFIRMED
        ));

        bookings.add(new Booking(
                2L,
                2L,
                2L,
                LocalDateTime.of(2026, 6, 25, 11, 0),
                LocalDateTime.of(2026, 6, 25, 12, 0),
                BookingStatus.PENDING
        ));
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookings
                .stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }

    public Booking createBooking(Long userId, Long serviceId, LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End time must not be before start time"
            );
        }
        Booking booking = new Booking(
                nextId,
                userId,
                serviceId,
                startTime,
                endTime,
                BookingStatus.PENDING
        );

        bookings.add(booking);
        nextId++;

        return booking;
    }
    public boolean deleteBookingById(Long id) {
        return bookings.removeIf(booking -> booking.getId().equals(id));
    }
    public Optional<Booking> cancelBookingById(Long id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .map(booking -> {
                    booking.setStatus(BookingStatus.CANCELLED);
                    return booking;
                });
    }

}