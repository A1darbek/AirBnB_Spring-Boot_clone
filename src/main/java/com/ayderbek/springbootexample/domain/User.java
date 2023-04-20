package com.ayderbek.springbootexample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    @JsonIgnore
    private List<Review> reviews;

    @ManyToMany
    @JsonIgnore
    private List<Host> hosts;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<WishList> wishLists;

//    @OneToMany(mappedBy = "User")
//    private List<Token> tokens;



    @OneToMany
    private List<Property> properties;

    @OneToMany
    private List<Reservation> reservations;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
