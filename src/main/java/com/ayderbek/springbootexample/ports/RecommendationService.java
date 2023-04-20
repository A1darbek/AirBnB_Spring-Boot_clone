package com.ayderbek.springbootexample.ports;


import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.repos.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ReviewRepository reviewRepository;

    public List<Property> getRecommendations(Integer userId) {
        List<Review> userReviews = reviewRepository.findByUser_Id(userId);
        List<Review> otherReviews = reviewRepository.findByUser_IdNot(userId);

        Map<Property, Double> propertyRatings = new HashMap<>();

        for (Review review : otherReviews) {
            Property property = review.getProperty();
            Double rating = propertyRatings.getOrDefault(property, 0.0);
            rating += review.getRating();
            propertyRatings.put(property, rating);
        }

        for (Map.Entry<Property, Double> entry : propertyRatings.entrySet()) {
            entry.setValue(entry.getValue() / otherReviews.size());
        }

        List<Property> recommendedProperties = new ArrayList<>();

        for (Map.Entry<Property, Double> entry : propertyRatings.entrySet()) {
            Property property = entry.getKey();
            if (!userReviews.stream().anyMatch(review -> review.getProperty().equals(property))) {
                recommendedProperties.add(property);
            }
            if (recommendedProperties.size() >= 10) {
                break;
            }
        }

        return recommendedProperties;
    }
}
