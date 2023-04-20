package com.ayderbek.springbootexample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "property")
public class Property {
    @Id
    @SequenceGenerator(
            name = "property_id_sequence",
            sequenceName = "property_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "property_id_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private String address;
    private String image;
    @ElementCollection
    private List<String> images;
    private String city;
    private String state;
    private String country;
    private BigDecimal price;
    private String type;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer maximumGuests;
    @ElementCollection
    private List<String> amenities;
    @ManyToOne
    @JoinColumn(name = "host_id")
    @JsonIgnore
    private Host host;

    @OneToMany
    @JsonIgnore
    private List<Reservation> reservations;

    @OneToMany
    @JsonIgnore
    private List<Review> reviews;



    @ManyToMany(mappedBy = "properties")
    @JsonIgnore
    private List<WishList> wishLists;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
