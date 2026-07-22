package com.viktor.booking.infrastructure.persistence.adapter;

import com.viktor.booking.application.repository.BookingRepository;
import com.viktor.booking.domain.enums.BookingStatus;
import com.viktor.booking.domain.model.Booking;
import com.viktor.booking.infrastructure.persistence.entity.BookableServiceEntity;
import com.viktor.booking.infrastructure.persistence.entity.BookingEntity;
import com.viktor.booking.infrastructure.persistence.entity.UserEntity;
import com.viktor.booking.infrastructure.persistence.mapper.BookingMapper;
import com.viktor.booking.infrastructure.persistence.repository.BookableServiceJpaRepository;
import com.viktor.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.viktor.booking.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@Transactional
public class JpaBookingRepositoryAdapter implements BookingRepository {

    private final BookingJpaRepository bookingJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookableServiceJpaRepository serviceJpaRepository;
    private final BookingMapper bookingMapper;

    public JpaBookingRepositoryAdapter(
            BookingJpaRepository bookingJpaRepository,
            UserJpaRepository userJpaRepository,
            BookableServiceJpaRepository serviceJpaRepository,
            BookingMapper bookingMapper
    ) {
        this.bookingJpaRepository = bookingJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.serviceJpaRepository = serviceJpaRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<Booking> findAll() {
        return bookingJpaRepository
                .findAll()
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingJpaRepository
                .findById(id)
                .map(bookingMapper::toDomain);
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookingJpaRepository
                .findByStatus(status)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public Booking save(Booking booking) {
        UserEntity userEntity = userJpaRepository
                .findById(booking.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with id: " + booking.getUserId()
                ));

        BookableServiceEntity serviceEntity = serviceJpaRepository
                .findById(booking.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bookable service not found with id: "
                                + booking.getServiceId()
                ));

        BookingEntity newEntity = bookingMapper.toNewEntity(
                booking,
                userEntity,
                serviceEntity
        );

        BookingEntity savedEntity =
                bookingJpaRepository.save(newEntity);

        return bookingMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        bookingJpaRepository.deleteById(id);
    }
}
