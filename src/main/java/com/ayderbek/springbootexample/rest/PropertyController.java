package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.Review;
import com.ayderbek.springbootexample.ports.ReviewService;
import com.ayderbek.springbootexample.request.PropertyRequest;
import com.ayderbek.springbootexample.repos.PropertyRepository;
import com.ayderbek.springbootexample.ports.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/properties")
@CrossOrigin("*")
@Slf4j
public class PropertyController {
    private final PropertyService propertyService;
    private final PropertyRepository propertyRepository;

    private final ReviewService reviewService;

    @GetMapping
    public List<Property> getAllProperties() {
        var properties = propertyService.getAllProperties();
        return properties;
    }

    @GetMapping("/currently-hosting")
    public ResponseEntity<List<Property>> getCurrentlyHostingProperties() {
        List<Property> properties = propertyService.findCurrentlyHostingProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Property>> getUpcomingProperties() {
        List<Property> properties = propertyService.findUpcomingProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/Checking-Out")
    public ResponseEntity<List<Property>> getCheckingOutProperties() {
        List<Property> properties = propertyService.findCheckingOutProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/Pending-Review")
    public ResponseEntity<List<Property>> getPendingReviewProperties() {
        List<Property> properties = propertyService.findPendingReviewProperties();
        return ResponseEntity.ok(properties);
    }


    @GetMapping("{property:\\d+}")
    public ResponseEntity getProfileById(Property property){
        return ResponseEntity.ok(property);
    }

    @PostMapping(
            path = "{property:\\d+}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadPropertyImage(@PathVariable Property property,
                                   @RequestPart("file") MultipartFile file){
        propertyService.uploadPropertyImage(property.getId(), file);
        property.setImage(file.getOriginalFilename());
        propertyRepository.save(property);
    }

    @PostMapping(
            path = "{property:\\d+}/images/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadPropertyImages(@PathVariable Property property,
                                    @RequestPart("file") MultipartFile file){
        propertyService.uploadPropertyImages(property.getId(), file);
        List<String> images = property.getImages();
        images.add(file.getOriginalFilename());
        property.setImages(images);
        propertyRepository.save(property);
    }

    @PostMapping
    public Property uploadImage(@RequestParam("bucketName") String bucketName,
                               @RequestParam("fileName") String fileName) throws IOException {
        return propertyService.saveImage(bucketName, fileName);
    }


@PostMapping("/create")
public ResponseEntity<Property> createProperty(@RequestBody PropertyRequest propertyRequest) {
    Property property = propertyService.create(propertyRequest);
    return ResponseEntity.ok(property);
}

    @PostMapping("/create-withHost")
    public ResponseEntity<Property> createWithHost(@RequestBody PropertyRequest propertyRequest) {
        Property property = propertyService.createWithHost(propertyRequest);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    public Property replaceProperty(@PathVariable Long id,@RequestBody PropertyRequest propertyRequest) {
        return propertyService.updateProperty(id, propertyRequest);
    }
    @PatchMapping("/{id}")
    public Property updateProperty(@PathVariable Long id, @RequestBody PropertyRequest propertyRequest) {
        return propertyService.patchProperty(id, propertyRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Property> searchProperties(@RequestParam String query) {
        List<Property> searchResults = propertyService.searchProperties(query);
        return searchResults;
    }

    @GetMapping("/by-max-price")
    public List<Property> getPropertiesByMaxPrice(@RequestParam BigDecimal maxPrice) {
        return propertyService.getPropertiesByMaxPrice(maxPrice);
    }

    @GetMapping("/by-bedrooms-and-bathrooms")
    public List<Property> getPropertiesByBedroomsAndBathrooms(@RequestParam Integer bedrooms, @RequestParam Integer bathrooms) {
        return propertyService.getPropertiesByBedroomsAndBathrooms(bedrooms, bathrooms);
    }

    @GetMapping("/avg-price")
    public ResponseEntity<BigDecimal> getAveragePriceByCountry(@RequestParam String country) {
        BigDecimal avgPrice = propertyService.getAveragePriceByCountry(country.trim());
        return avgPrice != null ? ResponseEntity.ok(avgPrice) : ResponseEntity.notFound().build();
    }


//    if (avgPrice != null) { return ResponseEntity.ok(avgPrice); } else { return ResponseEntity.notFound().build(); }

    @GetMapping("/by-min-bedrooms-bathrooms")
    public List<Property> getPropertiesByMinBedroomsAndBathrooms(
            @RequestParam Integer minBedrooms,
            @RequestParam Integer minBathrooms) {
        return propertyService.getPropertiesByMinBedroomsAndBathrooms(minBedrooms, minBathrooms);
    }


    @GetMapping("/{propertyId}/recent-reviews")
    public List<Review> getRecentReviewsForProperty(@PathVariable Long propertyId, @RequestParam int count) {
        Property property = propertyService.getPropertyById(propertyId);
        return reviewService.getRecentReviewsForProperty(property, count);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Property>> getTopRatedPropertiesByCountry(@RequestParam String country,
                                                                         @RequestParam int count) {
        List<Property> properties = propertyService.getTopRatedPropertiesByCountry(country, count);
      return !properties.isEmpty() ? ResponseEntity.ok(properties) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search-filter")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<Property> properties = propertyService.searchFilterProperties(keyword, city, state, country, minPrice, maxPrice);
        return ResponseEntity.ok(properties);
    }
}
