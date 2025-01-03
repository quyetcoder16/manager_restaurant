package com.promise.manager_restaurant.dto.response.rating_food;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingFoodResponse {
    String ratingFoodId;

    String comment;

    String foodId;

    String userId;

    int ratePoint;

    LocalDateTime createdAt;

}
