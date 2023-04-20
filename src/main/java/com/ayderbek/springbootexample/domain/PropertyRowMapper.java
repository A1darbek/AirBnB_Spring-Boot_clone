package com.ayderbek.springbootexample.domain;

import com.ayderbek.springbootexample.repos.HostRepository;
import com.ayderbek.springbootexample.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PropertyRowMapper implements RowMapper<Property> {

    private final HostRepository hostRepository;

    @Override
    public Property mapRow(ResultSet rs, int rowNum) throws SQLException {
        Property property = new Property();
        property.setId(rs.getLong("id"));
        property.setAddress(rs.getString("address"));
        property.setCity(rs.getString("city"));
        property.setCountry(rs.getString("country"));
        property.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        property.setDescription(rs.getString("description"));
        property.setImage(rs.getString("image"));
        property.setMaximumGuests(rs.getInt("maximum_guests"));
        property.setName(rs.getString("name"));
        property.setNumberOfBathrooms(rs.getInt("number_of_bathrooms"));
        property.setNumberOfBedrooms(rs.getInt("number_of_bedrooms"));
        property.setPrice(rs.getBigDecimal("price"));
        property.setState(rs.getString("state"));
        property.setType(rs.getString("type"));
        property.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        // retrieve the host and user entities
        Host host = hostRepository.findById(rs.getLong("host_id")).orElse(null);
        property.setHost(host);
        return property;
    }
}
