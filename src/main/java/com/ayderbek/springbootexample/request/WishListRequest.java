package com.ayderbek.springbootexample.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListRequest {
    private Integer userId;
    private List<Long> propertyIds;
    @NotNull
    private String name;
}
