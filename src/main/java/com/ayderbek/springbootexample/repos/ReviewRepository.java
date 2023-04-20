package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByUser_Id(Integer userId);
    List<Review> findByUser_IdNot(Integer userId);

    boolean existsByUserAndProperty(User user, Property property);
}
