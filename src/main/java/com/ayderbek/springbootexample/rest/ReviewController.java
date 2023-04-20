package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.ports.PropertyService;
import com.ayderbek.springbootexample.request.ReviewRequest;
import com.ayderbek.springbootexample.ports.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews")
@CrossOrigin("*")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    private final PropertyService propertyService;

    @GetMapping
    public List<Review> getAllReviews() {
        var reviews = reviewService.getAllReviews();
        return reviews;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public Review replaceReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest) {
        return reviewService.updateReview(id,reviewRequest);
    }

    @PatchMapping("/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest) {
        return reviewService.patchReview(id,reviewRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }



}
