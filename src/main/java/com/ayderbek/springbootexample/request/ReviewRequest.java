package com.ayderbek.springbootexample.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    @NotNull
    private String comment;
    @NotNull
    @Min(1)
    private Integer rating;

    @NotNull
    private Integer userId;

    @NotNull
    private Long propertyId;

//    @NotNull
//    private Long hostId;
}
