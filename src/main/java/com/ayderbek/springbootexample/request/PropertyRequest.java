package com.ayderbek.springbootexample.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String address;
    private String image;
    private String imageLink;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String country;
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String type;
    @NotNull
    @Min(1)
    private Integer numberOfBedrooms;
    @NotNull
    @Min(1)
    private Integer numberOfBathrooms;
    @NotNull
    @Min(1)
    private Integer maximumGuests;
    @NotEmpty
    private List<String> amenities;
    @NotNull
    private Long hostId;
}
