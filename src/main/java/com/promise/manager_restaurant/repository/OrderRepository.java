package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Orders;
import com.promise.manager_restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByRestaurant(Restaurant restaurant);

    List<Orders> findAllByRestaurantIn(List<Restaurant> restaurants);
}
