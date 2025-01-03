package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.RatingFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingFoodRepository extends JpaRepository<RatingFood, String> {
}
