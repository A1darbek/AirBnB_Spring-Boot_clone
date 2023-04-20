package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.domain.WishList;
import com.ayderbek.springbootexample.ports.WishListService;
import com.ayderbek.springbootexample.request.WishListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wishlist")
@CrossOrigin("*")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping
    public List<WishList> getAllWishLists() {
        var wishLists = wishListService.getAllWishLists();
        return wishLists;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createWishList(@RequestBody WishListRequest wishListRequest, @PathVariable Integer userId) {
        try {
            wishListService.createWishList(wishListRequest, userId);
            return new ResponseEntity<>("Wish list created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create wish list", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
