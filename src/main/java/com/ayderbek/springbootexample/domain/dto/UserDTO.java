package com.ayderbek.springbootexample.domain.dto;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Reservation;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
}
