package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Restaurant;
import com.promise.manager_restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    boolean existsRestaurantByTitle(String title);

    boolean existsRestaurantByEmail(String email);

    boolean existsRestaurantByPhone(String phone);

    Restaurant findByPhone(String phone);

    Restaurant findByEmail(String email);

    List<Restaurant> findByManager(User manager);
}
