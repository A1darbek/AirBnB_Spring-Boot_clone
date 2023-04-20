package com.ayderbek.springbootexample.exceptions;

public class ReservationConflictException extends RuntimeException{
    public ReservationConflictException(String reservation_conflicts_with_existing_reservations) {
        super("Reservation conflicts with existing reservations");
    }
}
