package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.food.AddFoodRequest;
import com.promise.manager_restaurant.dto.request.food.DeleteFoodRequest;
import com.promise.manager_restaurant.dto.request.food.UpadateFoodRequest;
import com.promise.manager_restaurant.dto.request.restaurant.AddCategoryRequest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRemoveFromResquest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.DeleteCategoryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantCreationRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantUpdateRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.food.FoodResponse;
import com.promise.manager_restaurant.dto.response.gallery.GalleryResponse;
import com.promise.manager_restaurant.dto.response.restaurant.RestaurantResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantManagementService {
    public List<RestaurantResponse> getAlLRestaurants();
    public RestaurantResponse createRestaurant(RestaurantCreationRequest request);
    public RestaurantResponse updateRestaurant(RestaurantUpdateRequest request, String id);
    public void deleteRestaurant(String id);
    public RestaurantResponse addGalleryToRestaurant(String id, GalleryRequest request);
    public List<GalleryResponse> getGalleries(String id);
    public void deleteGalleryFromRestaurant(GalleryRemoveFromResquest request);
    public void addCategoryToRestaurant(AddCategoryRequest request);
    public List<CategoryResponse> getCategories(String resId);
    public List<FoodResponse> getFoods(String resId);
    public void deleteCategoryFromRestaurant(DeleteCategoryRequest request);
    public void addFoodToRestaurant(AddFoodRequest request, String resId);
    public void updateFoodOfRestaurant(UpadateFoodRequest request, String resId);
    public void deleteFoodFromRestaurant(DeleteFoodRequest request);
}
