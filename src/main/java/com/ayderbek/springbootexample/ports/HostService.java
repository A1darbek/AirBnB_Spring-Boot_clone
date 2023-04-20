package com.ayderbek.springbootexample.ports;

import com.ayderbek.springbootexample.domain.Host;
import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.repos.HostRepository;
import com.ayderbek.springbootexample.request.HostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostService {
    private final PropertyService propertyService;
    private final UserService userService;

    private final HostRepository hostRepository;

    public List<Host> getAllHosts() {
        return hostRepository.findAll();
    }

    public Host createHost(HostRequest hostRequest) {
        Host host = new Host();

        List<Long> propertyIds = hostRequest.getPropertyIds();
        List<Property> properties = new ArrayList<>();
        for (Long propertyId : propertyIds) {
            Property property = propertyService.getPropertyById(propertyId);
            properties.add(property);
        }
        host.setProperties(properties);
        List<Integer> userIds = hostRequest.getUserIds();
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            User user = userService.getUserById(userId);
            users.add(user);
        }
        host.setUsers(users);
        host.setName(hostRequest.getName());

       return hostRepository.save(host);
    }

    public List<Host> findMostActiveHosts() {
        return hostRepository.findMostActiveHosts();
    }
}
