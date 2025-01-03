package com.promise.manager_restaurant.dto.response.rating_restaurant;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRestaurantResponse {
    String ratingResId;

    String comment;

    String resId;

    String userId;

    int ratePoint;

    LocalDateTime createdAt;

}
