package com.ayderbek.springbootexample.ports;

import com.ayderbek.springbootexample.domain.*;
import com.ayderbek.springbootexample.exceptions.ResourceNotFoundException;
import com.ayderbek.springbootexample.repos.PropertyRepository;
import com.ayderbek.springbootexample.repos.ReviewRepository;
import com.ayderbek.springbootexample.repos.UserRepository;
import com.ayderbek.springbootexample.request.ReviewRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

    private final EntityManager entityManager;

    public Review getReviewById(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("Reservation not found with id " + id);
        }
        return reviewOptional.get();
    }

    public Review createReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID " + reviewRequest.getUserId() + " not found"));
        Property property = propertyRepository.findById(reviewRequest.getPropertyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Property with ID " + reviewRequest.getPropertyId() + " not found"));
        if(reviewRepository.existsByUserAndProperty(user, property)) {
            throw new EntityNotFoundException("User has already reviewed this property");
        }
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        setDates(review);
        review.setUser(user);
        review.setProperty(property);
        return reviewRepository.save(review);
    }

    private static void setDates(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review updateReview(Long id,ReviewRequest request) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("Review not found with id " + id);
        }
        Review review = reviewOptional.get();
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }
        if (request.getRating() != null) {
            review.setRating(review.getRating());
        }
        return reviewRepository.save(review);
    }

    public Review patchReview(Long id,ReviewRequest request) {
        Review review = getReviewById(id);
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }
        if (request.getRating() != null) {
            review.setRating(review.getRating());
        }
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
            review.setUser(user);
        }
        if (request.getPropertyId() != null) {
            Property property = propertyRepository.findById(request.getPropertyId())
                    .orElseThrow(() -> new RuntimeException("there is no id "));
            review.setProperty(property);
        }
        return reviewRepository.save(review);
    }
    public void deleteReview(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            reviewRepository.delete(review);
        } else {
            throw new RuntimeException("there is no id ");
        }
    }


    public List<Review> getRecentReviewsForProperty(Property property, int count) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.property = :property ORDER BY r.createdAt DESC",
                Review.class
        );
        query.setParameter("property", property);
        query.setMaxResults(count);
        return query.getResultList();
    }

}
