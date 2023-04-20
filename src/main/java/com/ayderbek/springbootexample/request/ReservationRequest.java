package com.ayderbek.springbootexample.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    @NotNull
    @Min(1)
    private String status;

    @NotNull
    @Min(1)
    private Integer NumberOfGuests;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Integer userId;

    @NotNull
    private Long propertyId;

}
