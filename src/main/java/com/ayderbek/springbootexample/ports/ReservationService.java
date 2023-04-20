package com.ayderbek.springbootexample.ports;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Reservation;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.exceptions.ReservationConflictException;
import com.ayderbek.springbootexample.repos.ReviewRepository;
import com.ayderbek.springbootexample.request.ReservationRequest;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.exceptions.ResourceNotFoundException;
import com.ayderbek.springbootexample.repos.PropertyRepository;
import com.ayderbek.springbootexample.repos.ReservationRepository;
import com.ayderbek.springbootexample.repos.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

    private final NotificationService notificationService;

    private final ReviewRepository reviewRepository;

    private final EntityManager entityManager;



    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isEmpty()) {
            throw new EntityNotFoundException("Reservation not found with id " + id);
        }
        return reservationOptional.get();
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findReservationsArrivingSoon(User user, LocalDate arrivalDate) {
        return reservationRepository.findReservationsArrivingSoon(user, arrivalDate);
    }


    public Reservation addReviewToReservation(Long reservationId, Long reviewId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        reservation.setReview(review);
        return reservationRepository.save(reservation);
    }
    public Reservation createReservation(ReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID " + reservationRequest.getUserId() + " not found"));
        Property property = propertyRepository.findById(reservationRequest.getPropertyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Property with ID " + reservationRequest.getPropertyId() + " not found"));


        Reservation reservation = new Reservation();
        reservation.setStatus(reservationRequest.getStatus());
        reservation.setNumberOfGuests(reservationRequest.getNumberOfGuests());
        reservation.setTotalPrice(reservationRequest.getTotalPrice());
        setDates(reservation);
        reservation.setUser(user);
        reservation.setProperty(property);

        return reservationRepository.save(reservation);

    }

    private static void setDates(Reservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.of(2024,3,29));
        reservation.setCheckInDate(LocalDate.of(2023,4,16));
        reservation.setCheckOutDate(LocalDate.of(2023, 3,29));
    }

    public Reservation updateReservation(Long id,ReservationRequest request) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isEmpty()) {
            throw new EntityNotFoundException("Reservation not found with id " + id);
        }
        Reservation reservation = reservationOptional.get();

        if (request.getStatus() != null) {
            reservation.setStatus(request.getStatus());
        }
        if (request.getNumberOfGuests() != null) {
            reservation.setNumberOfGuests(request.getNumberOfGuests());
        }
        if (request.getTotalPrice() != null) {
            reservation.setTotalPrice(request.getTotalPrice());
        }
        return reservationRepository.save(reservation);
    }
    public Reservation patchReservation(Long id,ReservationRequest request) {
        Reservation reservation = getReservationById(id);
        if (request.getStatus() != null) {
            reservation.setStatus(request.getStatus());
        }
        if (request.getNumberOfGuests() != null) {
            reservation.setNumberOfGuests(request.getNumberOfGuests());
        }
        if (request.getTotalPrice() != null) {
            reservation.setTotalPrice(request.getTotalPrice());
        }
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
            reservation.setUser(user);
        }
        if (request.getPropertyId() != null) {
            Property property = propertyRepository.findById(request.getPropertyId())
                    .orElseThrow(() -> new RuntimeException("there is no id "));
            reservation.setProperty(property);
        }
        return reservationRepository.save(reservation);
    }
    public void deleteReservation(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservationRepository.delete(reservation);
        } else {
            throw new RuntimeException("there is no id ");
        }
    }


    public void confirmReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        String recipientEmail = reservation.getUser().getEmail();
        notificationService.sendNotificationEmail(recipientEmail,reservation);
    }

    public List<Reservation> findConflictingReservationsForProperty(Long propertyId, LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findConflictingReservationsForProperty(propertyId, startDate, endDate);
    }

    public List<Reservation> getReservationsForProperty(Property property) {
        TypedQuery<Reservation> query = entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.property = :property",
                Reservation.class
        );
        query.setParameter("property", property);
        return query.getResultList();
    }

    public List<Reservation> getReservationsNotReviewed() {
        TypedQuery<Reservation> query = entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.review IS NULL",
                Reservation.class
        );
        return query.getResultList();
    }
}
