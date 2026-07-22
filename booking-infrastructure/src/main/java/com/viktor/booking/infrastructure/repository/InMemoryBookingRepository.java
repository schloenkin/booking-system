package com.viktor.booking.infrastructure.repository;

import com.viktor.booking.application.repository.BookingRepository;
import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!jpa")
public class InMemoryBookingRepository implements BookingRepository {

    private final List<Booking> bookings = new ArrayList<>();
    private Long nextId = 3L;

    public InMemoryBookingRepository() {
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

    @Override
    public List<Booking> findAll() {
        return bookings;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookings
                .stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }
    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookings
                .stream()
                .filter(booking -> booking.getStatus() == status)
                .toList();
    }

    @Override
    public Booking save(Booking booking) {
        Booking savedBooking = new Booking(
                nextId,
                booking.getUserId(),
                booking.getServiceId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus()
        );

        bookings.add(savedBooking);
        nextId++;

        return savedBooking;
    }

    @Override
    public void deleteById(Long id) {
        bookings.removeIf(booking -> booking.getId().equals(id));
    }
}