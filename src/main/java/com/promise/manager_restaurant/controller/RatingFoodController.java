package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.rating_food.RatingFoodRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.rating_food.RatingFoodResponse;
import com.promise.manager_restaurant.service.RatingFoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratingfoods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingFoodController {

    RatingFoodService ratingFoodService;

    @PostMapping
    public ApiResponse<RatingFoodResponse> createRatingFood(@RequestBody RatingFoodRequest request) {
        return ApiResponse.<RatingFoodResponse>builder()
                .data(ratingFoodService.createRatingFood(request))
                .message("Create rating food successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRatingFood(@PathVariable String id) {
        ratingFoodService.deleteRatingFood(id);
        return ApiResponse.<Void>builder()
                .message("Delete rating food successfully")
                .build();
    }
}
