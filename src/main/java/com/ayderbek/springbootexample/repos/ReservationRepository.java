package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Reservation;
import com.ayderbek.springbootexample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.checkInDate <= :arrivalDate")
    List<Reservation> findReservationsArrivingSoon(@Param("user") User user, @Param("arrivalDate") LocalDate arrivalDate);

    @Query("SELECT r FROM Reservation r WHERE r.property.id = :propertyId AND r.status <> 'CANCELLED' AND ((r.startDate <= :startDate AND r.endDate >= :startDate) OR (r.startDate <= :endDate AND r.endDate >= :endDate))")
    List<Reservation> findConflictingReservationsForProperty(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(nativeQuery = true, value = "SELECT * FROM reservation r WHERE r.property_id = :propertyId AND r.status = 'BOOKED' AND (r.start_date, r.end_date) OVERLAPS (:startDate, :endDate)")
    List<Reservation> findConflictingReservations(@Param("propertyId") Long propertyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
