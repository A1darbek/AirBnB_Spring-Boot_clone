package com.ayderbek.springbootexample.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @SequenceGenerator(
            name = "review_id_sequence",
            sequenceName = "review_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_id_sequence"
    )
    private Long id;
    @ManyToOne
    private Property property;
    @ManyToOne
    private User user;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
