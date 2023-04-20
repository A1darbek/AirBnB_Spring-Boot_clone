package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.ports.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recommendations")
@CrossOrigin("*")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public List<Property> getRecommendations(@PathVariable Integer userId) {
        return recommendationService.getRecommendations(userId);
    }
}
