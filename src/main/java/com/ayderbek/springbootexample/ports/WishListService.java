package com.ayderbek.springbootexample.ports;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.domain.WishList;
import com.ayderbek.springbootexample.repos.WishListRepository;
import com.ayderbek.springbootexample.request.WishListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;

    private final UserService userService;

    private final PropertyService propertyService;

    public List<WishList> getAllWishLists() {
        return wishListRepository.findAll();
    }

    public void createWishList(WishListRequest wishListRequest, Integer userId) {
        User user = userService.getUserById(userId);
        WishList wishList = new WishList();
        wishList.setUser(user);

        List<Long> propertyIds = wishListRequest.getPropertyIds();
        List<Property> properties = new ArrayList<>();
        for (Long propertyId : propertyIds) {
            Property property = propertyService.getPropertyById(propertyId);
            properties.add(property);
        }
        wishList.setProperties(properties);
        wishList.setName(wishListRequest.getName());

        wishListRepository.save(wishList);
    }
}
