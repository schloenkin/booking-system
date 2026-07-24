package com.viktor.booking.application.service;

import com.viktor.booking.application.exception.InvalidBookingTimeException;
import com.viktor.booking.application.repository.BookingRepository;
import com.viktor.booking.application.exception.BookableServiceNotFoundException;
import com.viktor.booking.application.repository.BookableServiceRepository;
import com.viktor.booking.application.repository.UserRepository;
import com.viktor.booking.application.exception.UserNotFoundException;
import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;
import org.springframework.stereotype.Service;
import com.viktor.booking.application.exception.InactiveBookableServiceException;
import com.viktor.booking.domain.model.BookableService;
import com.viktor.booking.application.exception.InvalidBookingDurationException;
import com.viktor.booking.application.exception.BookingInPastException;
import com.viktor.booking.application.exception.BookingTimeConflictException;
import com.viktor.booking.application.exception.BookingAlreadyCancelledException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.Duration;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookableServiceRepository serviceRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            BookableServiceRepository serviceRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Transactional
    public Booking createBooking(Long userId, Long serviceId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidBookingTimeException(
                    "End time must be after start time"
            );
        }

        if (!startTime.isAfter(LocalDateTime.now())) {
            throw new BookingInPastException();
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        BookableService service = serviceRepository
                .findByIdForUpdate(serviceId)
                .orElseThrow(() ->
                        new BookableServiceNotFoundException(serviceId)
                );

        if (!service.isActive()) {
            throw new InactiveBookableServiceException(serviceId);
        }
        Duration bookingDuration =
                Duration.between(startTime, endTime);

        Duration requiredDuration =
                Duration.ofMinutes(service.getDurationMinutes());

        if (!bookingDuration.equals(requiredDuration)) {
            throw new InvalidBookingDurationException(
                    service.getDurationMinutes()
            );
        }

        if (bookingRepository.existsConflictingBooking(
                serviceId,
                startTime,
                endTime
        )) {
            throw new BookingTimeConflictException(serviceId);
        }

        Booking booking = new Booking(
                null,
                userId,
                serviceId,
                startTime,
                endTime,
                BookingStatus.PENDING
        );

        return bookingRepository.save(booking);
    }

    public boolean deleteBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }

    public Optional<Booking> cancelBookingById(Long id) {
        Optional<Booking> booking =
                bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return Optional.empty();
        }

        if (booking.get().getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException(id);
        }

        return bookingRepository.updateStatus(
                id,
                BookingStatus.CANCELLED
        );
    }

}