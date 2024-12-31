package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.request.food.AddFoodRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.food.FoodResponse;
import com.promise.manager_restaurant.entity.Category;
import com.promise.manager_restaurant.entity.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    @Mapping(target = "image", ignore = true)
    Food toFood(AddFoodRequest request);

    FoodResponse toFoodResponse(Food food);

}
