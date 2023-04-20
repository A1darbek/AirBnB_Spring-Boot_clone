package com.ayderbek.springbootexample.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @SequenceGenerator(
            name = "reservation_id_sequence",
            sequenceName = "reservation_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_id_sequence"
    )
    private Long id;
    @ManyToOne
    private Property property;
    @ManyToOne
    private User user;
    @OneToOne
    private Review review;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer NumberOfGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

}
