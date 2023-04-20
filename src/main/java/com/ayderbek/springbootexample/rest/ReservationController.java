package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Reservation;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.ports.PropertyService;
import com.ayderbek.springbootexample.ports.ReservationService;
import com.ayderbek.springbootexample.ports.UserService;
import com.ayderbek.springbootexample.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reservations")
@CrossOrigin("*")
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    private final UserService userService;

    private final PropertyService propertyService;

    @GetMapping("/arriving-soon")
    public List<Reservation> getReservationsArrivingSoon(@RequestParam("user-id") Integer userId,
                                                         @RequestParam("arrival-date")
                                                         @DateTimeFormat(pattern="yyyy-MM-dd")
                                                         String arrivalDateStr) {
        User user = userService.getUserById(userId);
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrivalDate = LocalDate.parse(arrivalDateStr.trim());
        log.info("the arrivalDate has been parsed" + arrivalDate);
        return reservationService.findReservationsArrivingSoon(user, arrivalDate);
    }


    @GetMapping
    public List<Reservation> getAllReservations() {
        var reservations = reservationService.getAllReservations();
        return reservations;
    }

    @PostMapping("/{reservationId}/review/{reviewId}")
    public Reservation addReviewToReservation(@PathVariable Long reservationId, @PathVariable Long reviewId) {
        return reservationService.addReviewToReservation(reservationId, reviewId);
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok(reservation);
    }


    @PutMapping("/{id}")
    public Reservation replaceReservation(@PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        return reservationService.updateReservation(id,reservationRequest);
    }


    @PatchMapping("/{id}")
    public Reservation updateReservation(@PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        return reservationService.patchReservation(id,reservationRequest);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        reservationService.confirmReservation(reservation);
        return ResponseEntity.ok("Reservation confirmed successfully!");
    }

    @GetMapping("/conflicts")
    public List<Reservation> findConflictingReservations(
            @RequestParam Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return reservationService.findConflictingReservationsForProperty(propertyId, startDate, endDate);
    }

    @GetMapping("/{propertyId}")
    public List<Reservation> getReservationsForProperty(@PathVariable Long propertyId) {
        Property property = propertyService.getPropertyById(propertyId);
        return reservationService.getReservationsForProperty(property);
    }

    @GetMapping("/not-reviewed")
    public List<Reservation> getReservationsNotReviewed() {
        return reservationService.getReservationsNotReviewed();
    }
}
