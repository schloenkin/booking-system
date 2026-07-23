package com.viktor.booking.infrastructure.persistence.adapter;

import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.enums.UserRole;
import com.viktor.booking.domain.model.Booking;
import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import com.viktor.booking.infrastructure.persistence.entity.BookingEntity;
import com.viktor.booking.infrastructure.persistence.entity.UserEntity;
import com.viktor.booking.infrastructure.persistence.mapper.BookingMapper;
import com.viktor.booking.infrastructure.persistence.repository.BookingJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("jpa")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ContextConfiguration(
        classes = JpaBookingRepositoryAdapterIntegrationTest.TestConfiguration.class
)
class JpaBookingRepositoryAdapterIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private JpaBookingRepositoryAdapter bookingRepositoryAdapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndFindBookingThroughAdapter() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "adapter-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Backend consultation",
                        "Individual backend consultation",
                        60,
                        true
                )
        );

        Long userId = user.getId();
        Long serviceId = service.getId();

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 5, 10, 0);

        LocalDateTime endTime =
                startTime.plusMinutes(60);

        Booking bookingToSave = new Booking(
                null,
                userId,
                serviceId,
                startTime,
                endTime,
                BookingStatus.PENDING
        );

        Booking savedBooking =
                bookingRepositoryAdapter.save(bookingToSave);

        assertThat(savedBooking.getId())
                .isNotNull();

        entityManager.flush();
        entityManager.clear();

        Booking foundBooking = bookingRepositoryAdapter
                .findById(savedBooking.getId())
                .orElseThrow();

        assertThat(foundBooking.getId())
                .isEqualTo(savedBooking.getId());

        assertThat(foundBooking.getUserId())
                .isEqualTo(userId);

        assertThat(foundBooking.getServiceId())
                .isEqualTo(serviceId);

        assertThat(foundBooking.getStartTime())
                .isEqualTo(startTime);

        assertThat(foundBooking.getEndTime())
                .isEqualTo(endTime);

        assertThat(foundBooking.getStatus())
                .isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldUpdateBookingStatusThroughAdapter() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "adapter-update-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "JPA consultation",
                        "Individual JPA consultation",
                        60,
                        true
                )
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 6, 10, 0);

        Booking bookingToSave = new Booking(
                null,
                user.getId(),
                service.getId(),
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        Booking savedBooking =
                bookingRepositoryAdapter.save(bookingToSave);

        entityManager.flush();
        entityManager.clear();

        Booking updatedBooking = bookingRepositoryAdapter
                .updateStatus(
                        savedBooking.getId(),
                        BookingStatus.CANCELLED
                )
                .orElseThrow();

        assertThat(updatedBooking.getStatus())
                .isEqualTo(BookingStatus.CANCELLED);

        entityManager.flush();
        entityManager.clear();

        Booking bookingFromDatabase = bookingRepositoryAdapter
                .findById(savedBooking.getId())
                .orElseThrow();

        assertThat(bookingFromDatabase.getStatus())
                .isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Testing consultation",
                        "Individual testing consultation",
                        60,
                        true
                )
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 7, 10, 0);

        Booking booking = new Booking(
                null,
                999999L,
                service.getId(),
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        assertThatThrownBy(() ->
                bookingRepositoryAdapter.save(booking)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with id: 999999");
    }

    @Test
    void shouldThrowExceptionWhenServiceDoesNotExist() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "missing-service-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 8, 10, 0);

        Booking booking = new Booking(
                null,
                user.getId(),
                999999L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        assertThatThrownBy(() ->
                bookingRepositoryAdapter.save(booking)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Bookable service not found with id: 999999"
                );
    }

    @Test
    void shouldFindBookingsByStatusThroughAdapter() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "adapter-status-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Spring Boot consultation",
                        "Individual Spring Boot consultation",
                        60,
                        true
                )
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 9, 10, 0);

        Booking pendingBooking = new Booking(
                null,
                user.getId(),
                service.getId(),
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        Booking cancelledBooking = new Booking(
                null,
                user.getId(),
                service.getId(),
                startTime.plusHours(2),
                startTime.plusHours(3),
                BookingStatus.CANCELLED
        );

        Booking savedPendingBooking =
                bookingRepositoryAdapter.save(pendingBooking);

        bookingRepositoryAdapter.save(cancelledBooking);

        entityManager.flush();
        entityManager.clear();

        var pendingBookings =
                bookingRepositoryAdapter.findByStatus(
                        BookingStatus.PENDING
                );

        assertThat(pendingBookings)
                .hasSize(1);

        assertThat(pendingBookings.get(0).getId())
                .isEqualTo(savedPendingBooking.getId());

        assertThat(pendingBookings.get(0).getStatus())
                .isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldDeleteBookingThroughAdapter() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "adapter-delete-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Docker consultation",
                        "Individual Docker consultation",
                        60,
                        true
                )
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        Booking booking = new Booking(
                null,
                user.getId(),
                service.getId(),
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        Booking savedBooking =
                bookingRepositoryAdapter.save(booking);

        entityManager.flush();
        entityManager.clear();

        bookingRepositoryAdapter.deleteById(
                savedBooking.getId()
        );

        entityManager.flush();
        entityManager.clear();

        assertThat(
                bookingRepositoryAdapter.findById(
                        savedBooking.getId()
                )
        ).isEmpty();
    }

    @Test
    void shouldDetectConflictingBookingThroughAdapter() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "conflict-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Conflict test service",
                        "Service for testing booking conflicts",
                        60,
                        true
                )
        );

        LocalDateTime existingStartTime =
                LocalDateTime.of(2030, 1, 10, 10, 0);

        Booking existingBooking = new Booking(
                null,
                user.getId(),
                service.getId(),
                existingStartTime,
                existingStartTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        bookingRepositoryAdapter.save(existingBooking);

        entityManager.flush();
        entityManager.clear();

        boolean conflict =
                bookingRepositoryAdapter.existsConflictingBooking(
                        service.getId(),
                        existingStartTime.plusMinutes(30),
                        existingStartTime.plusMinutes(90)
                );

        assertThat(conflict)
                .isTrue();
    }

    @Test
    void shouldNotDetectConflictForAdjacentBooking() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "adjacent-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Adjacent test service",
                        "Service for testing adjacent bookings",
                        60,
                        true
                )
        );

        LocalDateTime existingStartTime =
                LocalDateTime.of(2030, 1, 11, 10, 0);

        bookingRepositoryAdapter.save(
                new Booking(
                        null,
                        user.getId(),
                        service.getId(),
                        existingStartTime,
                        existingStartTime.plusMinutes(60),
                        BookingStatus.CONFIRMED
                )
        );

        entityManager.flush();
        entityManager.clear();

        boolean conflict =
                bookingRepositoryAdapter.existsConflictingBooking(
                        service.getId(),
                        existingStartTime.plusMinutes(60),
                        existingStartTime.plusMinutes(120)
                );

        assertThat(conflict)
                .isFalse();
    }

    @Test
    void shouldNotDetectConflictForCancelledBooking() {
        UserEntity user = entityManager.persistAndFlush(
                new UserEntity(
                        "cancelled-conflict-test@example.com",
                        "hashed-password",
                        UserRole.USER
                )
        );

        BookableServiceEntity service = entityManager.persistAndFlush(
                new BookableServiceEntity(
                        "Cancelled booking test service",
                        "Service for testing cancelled booking conflicts",
                        60,
                        true
                )
        );

        LocalDateTime existingStartTime =
                LocalDateTime.of(2030, 1, 12, 10, 0);

        bookingRepositoryAdapter.save(
                new Booking(
                        null,
                        user.getId(),
                        service.getId(),
                        existingStartTime,
                        existingStartTime.plusMinutes(60),
                        BookingStatus.CANCELLED
                )
        );

        entityManager.flush();
        entityManager.clear();

        boolean conflict =
                bookingRepositoryAdapter.existsConflictingBooking(
                        service.getId(),
                        existingStartTime.plusMinutes(30),
                        existingStartTime.plusMinutes(90)
                );

        assertThat(conflict)
                .isFalse();
    }

    @Configuration
    @EnableAutoConfiguration
    @EntityScan(
            basePackageClasses = {
                    BookingEntity.class,
                    UserEntity.class,
                    BookableServiceEntity.class
            }
    )
    @EnableJpaRepositories(
            basePackageClasses = BookingJpaRepository.class
    )
    @Import({
            JpaBookingRepositoryAdapter.class,
            BookingMapper.class
    })
    static class TestConfiguration {
    }
}
