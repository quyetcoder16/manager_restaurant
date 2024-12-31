package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Gallery;
import com.promise.manager_restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, String> {
    List<Gallery> findByRestaurant(Restaurant restaurant);
}
