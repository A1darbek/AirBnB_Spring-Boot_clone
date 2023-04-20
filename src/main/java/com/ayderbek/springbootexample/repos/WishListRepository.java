package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList,Long> {
}
