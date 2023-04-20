package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Long> {

    boolean existsByName(String name);
    @Query("SELECT DISTINCT p FROM Property p JOIN p.reservations r WHERE r.checkInDate <= :today AND r.checkOutDate >= :today")
    List<Property> findCurrentlyHostingProperties(@Param("today") LocalDate today);

    @Query("SELECT DISTINCT p , r.checkInDate FROM Property p JOIN p.reservations r WHERE r.checkInDate > :today ORDER BY r.checkInDate ASC")
    List<Property> findUpcomingProperties(@Param("today") LocalDate today);

    @Query("SELECT DISTINCT p ,r.checkOutDate FROM Property p JOIN p.reservations r WHERE r.checkOutDate = :today ORDER BY r.checkOutDate ASC")
    List<Property> findCheckingOutProperties(@Param("today") LocalDate today);

    @Query("SELECT DISTINCT p FROM Property p JOIN p.reservations r WHERE r.checkOutDate < :today AND r.review IS NULL")
    List<Property> findPendingReviewProperties(@Param("today") LocalDate today);

    @Query("SELECT p FROM Property p WHERE p.numberOfBedrooms >= :bedrooms AND p.numberOfBathrooms >= :bathrooms")
    List<Property> findPropertiesByBedroomsAndBathrooms(@Param("bedrooms") Integer bedrooms, @Param("bathrooms") Integer bathrooms);

    @Query("SELECT AVG(p.price) FROM Property p WHERE p.country = ?1")
    BigDecimal getAveragePriceByCountry(String country);

    @Query("SELECT p FROM Property p WHERE p.numberOfBedrooms >= :minBedrooms AND p.numberOfBathrooms >= :minBathrooms")
    List<Property> getPropertiesByMinBedroomsAndBathrooms(@Param("minBedrooms") Integer minBedrooms, @Param("minBathrooms") Integer minBathrooms);

    @Query("SELECT p FROM Property p JOIN p.reviews r WHERE p.country = :country GROUP BY p.id ORDER BY AVG(r.rating) DESC")
    List<Property> getTopRatedPropertiesByCountry(@Param("country") String country, Pageable pageable);
}
