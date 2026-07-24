package com.viktor.booking.domain.model;

import com.viktor.booking.domain.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import com.viktor.booking.domain.exception.BookingCannotBeConfirmedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    @Test
    void shouldConfirmPendingBooking() {
        LocalDateTime startTime =
                LocalDateTime.now().plusDays(1);

        Booking booking = new Booking(
                1L,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.PENDING
        );

        booking.confirm();

        assertThat(booking.getStatus())
                .isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldRejectConfirmationWhenBookingIsAlreadyConfirmed() {
        LocalDateTime startTime =
                LocalDateTime.now().plusDays(1);

        Booking booking = new Booking(
                1L,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CONFIRMED
        );

        assertThatThrownBy(booking::confirm)
                .isInstanceOf(
                        BookingCannotBeConfirmedException.class
                )
                .hasMessage(
                        "Booking cannot be confirmed from status: CONFIRMED"
                );

        assertThat(booking.getStatus())
                .isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldRejectConfirmationWhenBookingIsCancelled() {
        LocalDateTime startTime =
                LocalDateTime.now().plusDays(1);

        Booking booking = new Booking(
                1L,
                1L,
                2L,
                startTime,
                startTime.plusMinutes(60),
                BookingStatus.CANCELLED
        );

        assertThatThrownBy(booking::confirm)
                .isInstanceOf(
                        BookingCannotBeConfirmedException.class
                )
                .hasMessage(
                        "Booking cannot be confirmed from status: CANCELLED"
                );

        assertThat(booking.getStatus())
                .isEqualTo(BookingStatus.CANCELLED);
    }


}
