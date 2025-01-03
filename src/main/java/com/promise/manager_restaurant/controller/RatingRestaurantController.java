package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.rating_food.RatingFoodRequest;
import com.promise.manager_restaurant.dto.request.rating_restaurant.RatingRestaurantRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.rating_food.RatingFoodResponse;
import com.promise.manager_restaurant.dto.response.rating_restaurant.RatingRestaurantResponse;
import com.promise.manager_restaurant.service.RatingFoodService;
import com.promise.manager_restaurant.service.RatingRestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratingrestaurants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingRestaurantController {

    RatingRestaurantService ratingRestaurantService;

    @PostMapping
    public ApiResponse<RatingRestaurantResponse> createRatingFood(@RequestBody RatingRestaurantRequest request) {
        return ApiResponse.<RatingRestaurantResponse>builder()
                .data(ratingRestaurantService.createRatingRestaurant(request))
                .message("Create rating food successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRatingFood(@PathVariable String id) {
        ratingRestaurantService.deleteRatingRestaurant(id);
        return ApiResponse.<Void>builder()
                .message("Delete rating food successfully")
                .build();
    }
}
