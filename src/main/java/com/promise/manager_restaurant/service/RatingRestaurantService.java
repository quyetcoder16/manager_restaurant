package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.rating_restaurant.RatingRestaurantRequest;
import com.promise.manager_restaurant.dto.response.rating_restaurant.RatingRestaurantResponse;
import org.springframework.stereotype.Service;

@Service
public interface RatingRestaurantService {

    public RatingRestaurantResponse createRatingRestaurant(RatingRestaurantRequest request);
    public void deleteRatingRestaurant(String request);
}
