package com.viktor.booking.application.exception;

public class InvalidBookingSearchException
        extends RuntimeException {

  public InvalidBookingSearchException(String message) {
    super(message);
  }
}
