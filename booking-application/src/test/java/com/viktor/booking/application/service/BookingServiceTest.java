package com.viktor.booking.application.service;

import com.viktor.booking.application.exception.InvalidBookingTimeException;
import com.viktor.booking.application.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import com.viktor.booking.application.exception.UserNotFoundException;
import com.viktor.booking.application.exception.BookableServiceNotFoundException;
import com.viktor.booking.application.repository.UserRepository;
import com.viktor.booking.application.repository.BookableServiceRepository;
import com.viktor.booking.domain.enums.UserRole;
import com.viktor.booking.domain.model.User;
import com.viktor.booking.application.exception.InactiveBookableServiceException;
import com.viktor.booking.domain.model.BookableService;
import com.viktor.booking.application.exception.InvalidBookingDurationException;
import com.viktor.booking.application.exception.BookingInPastException;
import com.viktor.booking.application.exception.BookingTimeConflictException;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.never;
import static org.assertj.core.api.Assertions.assertThat;
import com.viktor.booking.application.exception.BookingAlreadyCancelledException;
import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookableServiceRepository serviceRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldRejectBookingWhenEndTimeEqualsStartTime() {
        LocalDateTime startTime = futureStartTime();

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        1L,
                        1L,
                        startTime,
                        startTime
                )
        )
                .isInstanceOf(InvalidBookingTimeException.class)
                .hasMessage(
                        "End time must be after start time"
                );

        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenUserDoesNotExist() {
        Long missingUserId = 999999L;

        LocalDateTime startTime = futureStartTime();

        when(userRepository.findById(missingUserId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        missingUserId,
                        1L,
                        startTime,
                        startTime.plusHours(1)
                )
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: 999999");

        verify(userRepository)
                .findById(missingUserId);

        verifyNoInteractions(bookingRepository);
    }
    @Test
    void shouldRejectBookingWhenServiceDoesNotExist() {
        Long userId = 1L;
        Long missingServiceId = 999999L;

        LocalDateTime startTime = futureStartTime();

        User existingUser = new User(
                userId,
                "booking-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(serviceRepository.findById(missingServiceId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        userId,
                        missingServiceId,
                        startTime,
                        startTime.plusHours(1)
                )
        )
                .isInstanceOf(
                        BookableServiceNotFoundException.class
                )
                .hasMessage(
                        "Bookable service not found with id: 999999"
                );

        verify(userRepository)
                .findById(userId);

        verify(serviceRepository)
                .findById(missingServiceId);

        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenServiceIsInactive() {
        Long userId = 1L;
        Long serviceId = 2L;

        LocalDateTime startTime = futureStartTime();

        User existingUser = new User(
                userId,
                "booking-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        BookableService inactiveService =
                new BookableService(
                        serviceId,
                        "Inactive consultation",
                        "This service is currently unavailable",
                        60,
                        false
                );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(inactiveService));

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        userId,
                        serviceId,
                        startTime,
                        startTime.plusHours(1)
                )
        )
                .isInstanceOf(
                        InactiveBookableServiceException.class
                )
                .hasMessage(
                        "Bookable service is inactive with id: 2"
                );

        verify(userRepository)
                .findById(userId);

        verify(serviceRepository)
                .findById(serviceId);

        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenDurationDoesNotMatchServiceDuration() {
        Long userId = 1L;
        Long serviceId = 2L;

        LocalDateTime startTime = futureStartTime();

        User existingUser = new User(
                userId,
                "booking-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        BookableService service = new BookableService(
                serviceId,
                "Java consultation",
                "Individual Java consultation",
                60,
                true
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(service));

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        userId,
                        serviceId,
                        startTime,
                        startTime.plusMinutes(30)
                )
        )
                .isInstanceOf(
                        InvalidBookingDurationException.class
                )
                .hasMessage(
                        "Booking duration must be exactly 60 minutes"
                );

        verify(userRepository)
                .findById(userId);

        verify(serviceRepository)
                .findById(serviceId);

        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenStartTimeIsInPast() {
        Long userId = 1L;
        Long serviceId = 2L;

        LocalDateTime startTime =
                LocalDateTime.now().minusDays(1);

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        userId,
                        serviceId,
                        startTime,
                        startTime.plusMinutes(60)
                )
        )
                .isInstanceOf(BookingInPastException.class)
                .hasMessage(
                        "Booking start time must be in the future"
                );

        verifyNoInteractions(
                userRepository,
                serviceRepository,
                bookingRepository
        );
    }

    @Test
    void shouldRejectBookingWhenRequestedTimeConflicts() {
        Long userId = 1L;
        Long serviceId = 2L;

        LocalDateTime startTime = futureStartTime();
        LocalDateTime endTime = startTime.plusMinutes(60);

        User existingUser = new User(
                userId,
                "booking-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        BookableService service = new BookableService(
                serviceId,
                "Java consultation",
                "Individual Java consultation",
                60,
                true
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(service));

        when(bookingRepository.existsConflictingBooking(
                serviceId,
                startTime,
                endTime
        )).thenReturn(true);

        assertThatThrownBy(() ->
                bookingService.createBooking(
                        userId,
                        serviceId,
                        startTime,
                        endTime
                )
        )
                .isInstanceOf(BookingTimeConflictException.class)
                .hasMessage(
                        "Bookable service is already booked "
                                + "for the requested time, service id: 2"
                );

        verify(userRepository)
                .findById(userId);

        verify(serviceRepository)
                .findById(serviceId);

        verify(bookingRepository)
                .existsConflictingBooking(
                        serviceId,
                        startTime,
                        endTime
                );

        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void shouldRejectCancellationWhenBookingIsAlreadyCancelled() {
        Long bookingId = 5L;
        LocalDateTime startTime = futureStartTime();

        Booking cancelledBooking = new Booking(
                bookingId,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CANCELLED
        );

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(cancelledBooking));

        assertThatThrownBy(() ->
                bookingService.cancelBookingById(bookingId)
        )
                .isInstanceOf(
                        BookingAlreadyCancelledException.class
                )
                .hasMessage(
                        "Booking is already cancelled with id: 5"
                );

        verify(bookingRepository)
                .findById(bookingId);

        verify(bookingRepository, never())
                .updateStatus(
                        bookingId,
                        BookingStatus.CANCELLED
                );
    }

    @Test
    void shouldCancelPendingBooking() {
        Long bookingId = 6L;
        LocalDateTime startTime = futureStartTime();

        Booking pendingBooking = new Booking(
                bookingId,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        Booking cancelledBooking = new Booking(
                bookingId,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CANCELLED
        );

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(pendingBooking));

        when(bookingRepository.updateStatus(
                bookingId,
                BookingStatus.CANCELLED
        )).thenReturn(Optional.of(cancelledBooking));

        Optional<Booking> result =
                bookingService.cancelBookingById(bookingId);

        assertThat(result)
                .containsSame(cancelledBooking);

        verify(bookingRepository)
                .findById(bookingId);

        verify(bookingRepository)
                .updateStatus(
                        bookingId,
                        BookingStatus.CANCELLED
                );
    }

    @Test
    void shouldCancelConfirmedBooking() {
        Long bookingId = 7L;
        LocalDateTime startTime = futureStartTime();

        Booking confirmedBooking = new Booking(
                bookingId,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CONFIRMED
        );

        Booking cancelledBooking = new Booking(
                bookingId,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CANCELLED
        );

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(confirmedBooking));

        when(bookingRepository.updateStatus(
                bookingId,
                BookingStatus.CANCELLED
        )).thenReturn(Optional.of(cancelledBooking));

        Optional<Booking> result =
                bookingService.cancelBookingById(bookingId);

        assertThat(result)
                .containsSame(cancelledBooking);

        verify(bookingRepository)
                .findById(bookingId);

        verify(bookingRepository)
                .updateStatus(
                        bookingId,
                        BookingStatus.CANCELLED
                );
    }

    @Test
    void shouldCreateBookingWhenAllBusinessRulesAreSatisfied() {
        Long userId = 1L;
        Long serviceId = 2L;

        LocalDateTime startTime = futureStartTime();
        LocalDateTime endTime = startTime.plusMinutes(60);

        User existingUser = new User(
                userId,
                "booking-user@example.com",
                "hashed-password",
                UserRole.USER
        );

        BookableService activeService = new BookableService(
                serviceId,
                "Java consultation",
                "Individual Java consultation",
                60,
                true
        );

        Booking savedBooking = new Booking(
                10L,
                userId,
                serviceId,
                startTime,
                endTime,
                BookingStatus.PENDING
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(activeService));

        when(bookingRepository.existsConflictingBooking(
                serviceId,
                startTime,
                endTime
        )).thenReturn(false);

        ArgumentCaptor<Booking> bookingCaptor =
                ArgumentCaptor.forClass(Booking.class);

        when(bookingRepository.save(bookingCaptor.capture()))
                .thenReturn(savedBooking);

        Booking result = bookingService.createBooking(
                userId,
                serviceId,
                startTime,
                endTime
        );

        Booking bookingPassedToRepository =
                bookingCaptor.getValue();

        assertThat(bookingPassedToRepository.getId())
                .isNull();

        assertThat(bookingPassedToRepository.getUserId())
                .isEqualTo(userId);

        assertThat(bookingPassedToRepository.getServiceId())
                .isEqualTo(serviceId);

        assertThat(bookingPassedToRepository.getStartTime())
                .isEqualTo(startTime);

        assertThat(bookingPassedToRepository.getEndTime())
                .isEqualTo(endTime);

        assertThat(bookingPassedToRepository.getStatus())
                .isEqualTo(BookingStatus.PENDING);

        assertThat(result)
                .isSameAs(savedBooking);
    }

    private LocalDateTime futureStartTime() {
        return LocalDateTime.now()
                .plusDays(1)
                .withSecond(0)
                .withNano(0);
    }
}
