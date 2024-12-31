package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantCreationRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantUpdateRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.restaurant.RestaurantResponse;
import com.promise.manager_restaurant.entity.Category;
import com.promise.manager_restaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Named("mapStringToBoolean")
    default Boolean mapStringToBoolean(String value) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return Boolean.valueOf(value);
    }

    @Mapping(target = "manager", ignore = true) // Bỏ qua manager vì không có trong request
    @Mapping(target = "listGallery", ignore = true)
    @Mapping(target = "listFood", ignore = true)
    @Mapping(target = "listRatingRestaurant", ignore = true)
    @Mapping(target = "listRestaurantCategory", ignore = true)
    @Mapping(target = "listPromo", ignore = true)
    @Mapping(target = "listOrder", ignore = true)
    @Mapping(target = "resID", ignore = true)
    @Mapping(target = "background", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "isFreeship", source = "isFreeship", qualifiedByName = "mapStringToBoolean")
    Restaurant tRestaurant(RestaurantCreationRequest request);

    @Mapping(target = "manager", ignore = true) // Bỏ qua manager vì không có trong request
    @Mapping(target = "listGallery", ignore = true)
    @Mapping(target = "listFood", ignore = true)
    @Mapping(target = "listRatingRestaurant", ignore = true)
    @Mapping(target = "listRestaurantCategory", ignore = true)
    @Mapping(target = "listPromo", ignore = true)
    @Mapping(target = "listOrder", ignore = true)
    @Mapping(target = "resID", ignore = true)
    @Mapping(target = "background", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "isFreeship", source = "isFreeship", qualifiedByName = "mapStringToBoolean")
    Restaurant tRestaurant(RestaurantUpdateRequest request);


    RestaurantResponse tRestaurantResponse(Restaurant restaurant);

}
