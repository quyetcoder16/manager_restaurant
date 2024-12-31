package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, String> {
}
