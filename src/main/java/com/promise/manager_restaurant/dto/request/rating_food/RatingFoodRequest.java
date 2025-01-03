package com.promise.manager_restaurant.dto.request.rating_food;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingFoodRequest {
    String comment;
    String foodId;
    int ratePoint;
}
