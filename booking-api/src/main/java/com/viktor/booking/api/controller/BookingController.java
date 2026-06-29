package com.viktor.booking.api.controller;

import com.viktor.booking.api.dto.BookingResponse;
import com.viktor.booking.application.service.BookingQueryService;
import com.viktor.booking.domain.model.Booking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {

    private final BookingQueryService bookingQueryService;

    public BookingController(BookingQueryService bookingQueryService) {
        this.bookingQueryService = bookingQueryService;
    }

    @GetMapping("/api/bookings")
    public List<BookingResponse> getBookings() {
        return bookingQueryService.getAllBookings()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getServiceId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus().name()
        );
    }
}