package com.viktor.booking.api.controller;

import com.viktor.booking.api.dto.BookingResponse;
import com.viktor.booking.application.service.BookingService;
import com.viktor.booking.domain.model.Booking;
import com.viktor.booking.domain.enums.BookingStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import com.viktor.booking.api.dto.BookingCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.viktor.booking.api.dto.PageResponse;
import com.viktor.booking.application.query.BookingSearchCriteria;
import com.viktor.booking.application.query.PageRequestData;
import com.viktor.booking.application.query.PageResult;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.time.LocalDateTime;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/api/bookings")
    public PageResponse<BookingResponse> searchBookings(
            @RequestParam(
                    name = "status",
                    required = false
            )
            BookingStatus status,

            @RequestParam(
                    name = "userId",
                    required = false
            )
            Long userId,

            @RequestParam(
                    name = "serviceId",
                    required = false
            )
            Long serviceId,

            @RequestParam(
                    name = "from",
                    required = false
            )
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime from,

            @RequestParam(
                    name = "to",
                    required = false
            )
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime to,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "20"
            )
            int size,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = "startTime"
            )
            String sortBy,

            @RequestParam(
                    name = "direction",
                    defaultValue = "asc"
            )
            String direction
    ) {
        BookingSearchCriteria criteria =
                new BookingSearchCriteria(
                        status,
                        userId,
                        serviceId,
                        from,
                        to
                );

        PageRequestData pageRequest =
                new PageRequestData(
                        page,
                        size,
                        sortBy,
                        direction
                );

        PageResult<Booking> result =
                bookingService.searchBookings(
                        criteria,
                        pageRequest
                );

        return toPageResponse(result);
    }

    @GetMapping("/api/bookings/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable("id") Long id) {
        return bookingService.getBookingById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/api/bookings/status/{status}")
    public List<BookingResponse> getBookingsByStatus(@PathVariable("status") BookingStatus status) {
        return bookingService.getBookingsByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @DeleteMapping("/api/bookings/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable("id") Long id) {
        boolean deleted = bookingService.deleteBookingById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
    @PutMapping("/api/bookings/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBookingById(@PathVariable("id") Long id) {
        return bookingService.cancelBookingById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/api/bookings/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBookingById(
            @PathVariable("id") Long id
    ) {
        return bookingService.confirmBookingById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/api/bookings")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        Booking booking = bookingService.createBooking(
                request.getUserId(),
                request.getServiceId(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.status(201).body(toResponse(booking));
    }

    private PageResponse<BookingResponse> toPageResponse(
            PageResult<Booking> result
    ) {
        List<BookingResponse> content = result
                .content()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
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