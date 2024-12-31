package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Restaurant;
import com.promise.manager_restaurant.entity.RestaurantCategory;
import com.promise.manager_restaurant.entity.keys.KeyRestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, KeyRestaurantCategory> {
    List<RestaurantCategory> findByRestaurant(Restaurant restaurant);
}
