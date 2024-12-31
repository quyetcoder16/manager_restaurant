package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.food.AddFoodRequest;
import com.promise.manager_restaurant.dto.request.food.DeleteFoodRequest;
import com.promise.manager_restaurant.dto.request.food.UpadateFoodRequest;
import com.promise.manager_restaurant.dto.request.restaurant.AddCategoryRequest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRemoveFromResquest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.DeleteCategoryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantCreationRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantUpdateRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.food.FoodResponse;
import com.promise.manager_restaurant.dto.response.gallery.GalleryResponse;
import com.promise.manager_restaurant.dto.response.restaurant.RestaurantResponse;
import com.promise.manager_restaurant.service.RestaurantManagementService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantManagementController {

    RestaurantManagementService restaurantManagementService;

    @GetMapping
    ApiResponse<List<RestaurantResponse>> getAllRestaurants() {
        return ApiResponse.<List<RestaurantResponse>>builder()
                .data(restaurantManagementService.getAlLRestaurants())
                .message("Get all restaurants successfully")
                .build();
    }

    @PostMapping
    ApiResponse<RestaurantResponse> createRestaurant(@ModelAttribute @Valid RestaurantCreationRequest request) {
        return ApiResponse.<RestaurantResponse>builder()
                .data(restaurantManagementService.createRestaurant(request))
                .message("Create restaurant successfully!")
                .build();
    }

    @PutMapping("/{resId}")
    ApiResponse<RestaurantResponse> updateRestaurant(@PathVariable String resId,
                                                     @ModelAttribute @Valid RestaurantUpdateRequest request) {
        return ApiResponse.<RestaurantResponse>builder()
                .data(restaurantManagementService.updateRestaurant(request, resId))
                .message("Update restaurant succesfully")
                .build();
    }

    @DeleteMapping("/{resId}")
    ApiResponse<Void> deleteRestaurant(@PathVariable String resId) {
        restaurantManagementService.deleteRestaurant(resId);
        return ApiResponse.<Void>builder()
                .message("Delete restaurant successfully")
                .build();
    }

    @GetMapping("galleries/{resId}")
    ApiResponse<List<GalleryResponse>> getAllGalleries(@PathVariable String resId) {
        return ApiResponse.<List<GalleryResponse>>builder()
                .data(restaurantManagementService.getGalleries(resId))
                .message("Get all galleries successfully")
                .build();
    }

    @PostMapping("galleries/{resId}")
    ApiResponse<RestaurantResponse> addGalleryToRestaurant(@PathVariable String resId,
                                                           @ModelAttribute GalleryRequest request) {
        return ApiResponse.<RestaurantResponse>builder()
                .data(restaurantManagementService.addGalleryToRestaurant(resId, request))
                .message("Add gallery successfully")
                .build();
    }

    @DeleteMapping("galleries")
    ApiResponse<RestaurantResponse> addGalleryToRestaurant(@RequestBody GalleryRemoveFromResquest request) {
        restaurantManagementService.deleteGalleryFromRestaurant(request);
        return ApiResponse.<RestaurantResponse>builder()
                .message("Delete gallery successfully")
                .build();
    }

    @GetMapping("categories/{resId}")
    ApiResponse<List<CategoryResponse>> getAllCategoriesOfRes(@PathVariable String resId) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(restaurantManagementService.getCategories(resId))
                .message("Get all categories successfully")
                .build();
    }

    ;

    @PostMapping("categories")
    ApiResponse<Void> addCategoryToRestaurant(@RequestBody AddCategoryRequest request) {
        restaurantManagementService.addCategoryToRestaurant(request);
        return ApiResponse.<Void>builder()
                .message("Add category successfully")
                .build();
    }

    @DeleteMapping("categories")
    ApiResponse<Void> deleteCategoryFromRestaurant(@RequestBody DeleteCategoryRequest request) {
        restaurantManagementService.deleteCategoryFromRestaurant(request);
        return ApiResponse.<Void>builder()
                .message("Delete category successfully")
                .build();
    }

    @GetMapping("foods/{resId}")
    ApiResponse<List<FoodResponse>> getFoodOfRestaurant(@PathVariable String resId) {
        return ApiResponse.<List<FoodResponse>>builder()
                .data(restaurantManagementService.getFoods(resId))
                .message("Get all categories successfully")
                .build();
    }

    @PostMapping("foods/{resId}")
    ApiResponse<Void> addFoodToRestaurant(@PathVariable String resId,
                                          @ModelAttribute AddFoodRequest request) {
        restaurantManagementService.addFoodToRestaurant(request, resId);
        return ApiResponse.<Void>builder()
                .message("Add food to restaurant successfully")
                .build();
    }

    @PutMapping("foods/{resId}")
    ApiResponse<Void> updateFoodOrRestaurant(@PathVariable String resId,
                                          @ModelAttribute UpadateFoodRequest request) {
        restaurantManagementService.updateFoodOfRestaurant(request, resId);
        return ApiResponse.<Void>builder()
                .message("Update food of restaurant successfully")
                .build();
    }

    @DeleteMapping("foods")
    ApiResponse<Void> deleteFoodFromRes(@RequestBody DeleteFoodRequest request) {
        restaurantManagementService.deleteFoodFromRestaurant(request);
        return ApiResponse.<Void>builder()
                .message("Delete food from restaurant successfully")
                .build();
    }
}
