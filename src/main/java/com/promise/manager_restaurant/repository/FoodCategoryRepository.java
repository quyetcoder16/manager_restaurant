package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.FoodCategory;
import com.promise.manager_restaurant.entity.Restaurant;
import com.promise.manager_restaurant.entity.RestaurantCategory;
import com.promise.manager_restaurant.entity.keys.KeyFoodCategory;
import com.promise.manager_restaurant.entity.keys.KeyRestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, KeyFoodCategory> {
}
