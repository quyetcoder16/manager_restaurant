package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.rating_food.RatingFoodRequest;
import com.promise.manager_restaurant.dto.response.rating_food.RatingFoodResponse;
import org.springframework.stereotype.Service;

@Service
public interface RatingFoodService {
    public RatingFoodResponse createRatingFood(RatingFoodRequest ratingFoodRequest);
    public void deleteRatingFood(String request);
}
