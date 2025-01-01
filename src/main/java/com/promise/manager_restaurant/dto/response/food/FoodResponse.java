package com.promise.manager_restaurant.dto.response.food;

import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodResponse {

    String foodId;

    String title;

    String description;

    String image;

    String timeShip;

    double price;

    CategoryResponse category;
}
