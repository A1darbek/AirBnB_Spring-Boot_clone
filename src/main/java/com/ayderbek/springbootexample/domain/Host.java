package com.ayderbek.springbootexample.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private List<Property> properties;

    @ManyToMany
    @JoinTable(
            name = "host_user",
            joinColumns = @JoinColumn(name = "host_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
    private String name;


}
