package com.ayderbek.springbootexample.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostRequest {
    private List<Long> propertyIds;
    private List<Integer> userIds;
    private String name;
}
