package com.ayderbek.springbootexample.ports;

import com.ayderbek.springbootexample.bucket.BucketName;
import com.ayderbek.springbootexample.domain.*;
import com.ayderbek.springbootexample.exceptions.ConflictException;
import com.ayderbek.springbootexample.repos.HostRepository;
import com.ayderbek.springbootexample.request.PropertyRequest;
import com.ayderbek.springbootexample.filestore.FileStore;
import com.ayderbek.springbootexample.repos.PropertyRepository;
import com.ayderbek.springbootexample.repos.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDate.*;
import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final FileStore fileStore;

    private final PropertyRepository propertyRepository;

    private final UserRepository userRepository;

    private final HostRepository hostRepository;

    private final JdbcTemplate jdbcTemplate;

    private final S3Service s3Service;

    private final EntityManager entityManager;


    public Property saveImage(String bucketName, String fileName) throws IOException {
        byte[] imageData = s3Service.downloadImage(bucketName, fileName);
        Property property = new Property();
        property.builder().image(String.valueOf(imageData))
                .build();

        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public void uploadPropertyImage(Long propertyId, MultipartFile file) {
        isFileEmpty(file);
        isImage(file);

        Property property = getProfileorThrow(propertyId);


        Map<String, String> metadata = extractMetadata(file);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), property.getId());

        String filename = String.format("%s-%s", file.getOriginalFilename(), propertyId);


        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            property.setImage(filename);
//             profileRepository.save(property);


        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void uploadPropertyImages(Long propertyId, MultipartFile file) {
        isFileEmpty(file);
        isImage(file);

        Property property = getProfileorThrow(propertyId);


        Map<String, String> metadata = extractMetadata(file);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), property.getId());

        String filename = String.format("%s-%s", file.getOriginalFilename(), propertyId);


        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            List<String> images = property.getImages();
            if (images == null) {
                images = List.of();
            }
            images.add(file.getOriginalFilename());
            property.setImages(images);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    static Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    Property getProfileorThrow(Long propertyId) {
        return propertyRepository
                .findAllById(Collections.singleton(propertyId))
                .stream()
                .filter(profile -> profile.getId().equals(propertyId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", propertyId)));
    }

    private static void isImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_SVG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image");
        }
    }

    private static void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    public Property getPropertyById(Long id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isEmpty()) {
            throw new EntityNotFoundException("Property not found with id " + id);
        }
        return propertyOptional.get();
    }

    public List<Property> findCurrentlyHostingProperties() {
        LocalDate today = now();
        return propertyRepository.findCurrentlyHostingProperties(today);
    }

    public List<Property> findUpcomingProperties() {
        LocalDate today  = now();
        return propertyRepository.findUpcomingProperties(today);
    }

    public List<Property> findCheckingOutProperties() {
        LocalDate today  = now();
        return propertyRepository.findCheckingOutProperties(today);
    }

    public List<Property> findPendingReviewProperties() {
        LocalDate today = LocalDate.now();
        return propertyRepository.findPendingReviewProperties(today);
    }

    public Property create(PropertyRequest propertyRequest) {
        if (propertyRepository.existsByName(propertyRequest.getName())) {
            throw new ConflictException("Property with name " + propertyRequest.getName() + " already exists");
        }
            Property property = new Property();
            property.setName(propertyRequest.getName());
            property.setDescription(propertyRequest.getDescription());
            property.setAddress(propertyRequest.getAddress());
            property.setCity(propertyRequest.getCity());
            property.setState(propertyRequest.getState());
            property.setCountry(propertyRequest.getCountry());
            property.setPrice(propertyRequest.getPrice());
            property.setType(propertyRequest.getType());
            property.setNumberOfBedrooms(propertyRequest.getNumberOfBedrooms());
            property.setNumberOfBathrooms(propertyRequest.getNumberOfBathrooms());
            property.setMaximumGuests(propertyRequest.getMaximumGuests());
            property.setAmenities(propertyRequest.getAmenities());
            setDates(property);

        return propertyRepository.save(property);
    }

    private Host getWithHost(PropertyRequest propertyRequest, Property property) {
        Host host = hostRepository.findById(propertyRequest.getHostId())
                .orElseThrow(() -> new EntityNotFoundException("Host with ID " + propertyRequest.getHostId() + " not found"));
        property.setHost(host);
        return host;
    }

    public Property createWithHost(PropertyRequest propertyRequest){
        if (propertyRepository.existsByName(propertyRequest.getName())) {
            throw new ConflictException("Property with name " + propertyRequest.getName() + " already exists");
        }
        Property property = new Property();
        Host host = getWithHost(propertyRequest, property);
        property.setName(propertyRequest.getName());
        property.setDescription(propertyRequest.getDescription());
        property.setAddress(propertyRequest.getAddress());
        property.setCity(propertyRequest.getCity());
        property.setState(propertyRequest.getState());
        property.setCountry(propertyRequest.getCountry());
        property.setPrice(propertyRequest.getPrice());
        property.setType(propertyRequest.getType());
        property.setNumberOfBedrooms(propertyRequest.getNumberOfBedrooms());
        property.setNumberOfBathrooms(propertyRequest.getNumberOfBathrooms());
        property.setMaximumGuests(propertyRequest.getMaximumGuests());
        property.setAmenities(propertyRequest.getAmenities());
        property.setHost(host);
        setDates(property);

        return propertyRepository.save(property);
    }

    private static void setDates(Property property) {
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
    }
    public Property updateProperty(Long id, PropertyRequest request) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isEmpty()) {
            throw new EntityNotFoundException("Property not found with id " + id);
        }

        Property property = propertyOptional.get();

        if (request.getName() != null) {
            property.setName(request.getName());
        }
        if (request.getDescription() != null) {
            property.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            property.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            property.setCity(request.getCity());
        }
        if (request.getState() != null) {
            property.setState(request.getState());
        }
        if (request.getCountry() != null) {
            property.setCountry(request.getCountry());
        }
        if (request.getPrice() != null) {
            property.setPrice(request.getPrice());
        }
        if (request.getType() != null) {
            property.setType(request.getType());
        }
        if (request.getNumberOfBedrooms() != null) {
            property.setNumberOfBedrooms(request.getNumberOfBedrooms());
        }
        if (request.getNumberOfBathrooms() != null) {
            property.setNumberOfBathrooms(request.getNumberOfBathrooms());
        }
        if (request.getMaximumGuests() != null) {
            property.setMaximumGuests(request.getMaximumGuests());
        }
        if (request.getAmenities() != null) {
            property.setAmenities(request.getAmenities());
        }

        return propertyRepository.save(property);
    }
    public Property patchProperty(Long id, PropertyRequest request) {
        Property property = getPropertyById(id);
        if (request.getName() != null) {
            property.setName(request.getName());
        }
        if (request.getDescription() != null) {
            property.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            property.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            property.setCity(request.getCity());
        }
        if (request.getState() != null) {
            property.setState(request.getState());
        }
        if (request.getCountry() != null) {
            property.setCountry(request.getCountry());
        }
        if (request.getPrice() != null) {
            property.setPrice(request.getPrice());
        }
        if (request.getType() != null) {
            property.setType(request.getType());
        }
        if (request.getNumberOfBedrooms() != null) {
            property.setNumberOfBedrooms(request.getNumberOfBedrooms());
        }
        if (request.getNumberOfBathrooms() != null) {
            property.setNumberOfBathrooms(request.getNumberOfBathrooms());
        }
        if (request.getMaximumGuests() != null) {
            property.setMaximumGuests(request.getMaximumGuests());
        }
        if (request.getAmenities() != null) {
            property.setAmenities(request.getAmenities());
        }
        return propertyRepository.save(property);
    }
    public void deleteProperty(Long id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            propertyRepository.delete(property);
        } else {
            throw new RuntimeException("there is no id ");
        }
    }

    public List<Property> searchProperties(String query) {
        String sql = "SELECT * FROM search_properties(?)";
        return jdbcTemplate.query(sql, ps -> ps.setString(1, query)
                , new PropertyRowMapper(hostRepository));
    }


    public List<Property> getPropertiesByMaxPrice(BigDecimal maxPrice) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.price <= :maxPrice",
                Property.class
        );
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

    public List<Property> getPropertiesByBedroomsAndBathrooms(Integer bedrooms, Integer bathrooms) {
        return propertyRepository.findPropertiesByBedroomsAndBathrooms(bedrooms, bathrooms);
    }

    public BigDecimal getAveragePriceByCountry(String country) {
        return propertyRepository.getAveragePriceByCountry(country);
    }

    public List<Property> getPropertiesByMinBedroomsAndBathrooms(Integer minBedrooms, Integer minBathrooms) {
        return propertyRepository.getPropertiesByMinBedroomsAndBathrooms(minBedrooms, minBathrooms);
    }

    public List<Property> getTopRatedPropertiesByCountry(String country, int count) {
        return propertyRepository.getTopRatedPropertiesByCountry(country, Pageable.ofSize(count));
    }

    public List<Property> searchFilterProperties(String keyword, String city, String state, String country, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> cq = cb.createQuery(Property.class);
        Root<Property> root = cq.from(Property.class);

        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            predicates.add(cb.or(
                    cb.like(root.get("name"), "%" + keyword + "%"),
                    cb.like(root.get("description"), "%" + keyword + "%")
            ));
        }
        if (city != null && !city.isEmpty()) {
            predicates.add(cb.equal(root.get("city"), city));
        }
        if (state != null && !state.isEmpty()) {
            predicates.add(cb.equal(root.get("state"), state));
        }
        if (country != null && !country.isEmpty()) {
            predicates.add(cb.equal(root.get("country"), country));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Property> query = entityManager.createQuery(cq);

        return query.getResultList();
    }

}
