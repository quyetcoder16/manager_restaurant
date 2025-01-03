package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.RatingRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRestaurantRepository extends JpaRepository<RatingRestaurant, String> {
}
