package com.promise.manager_restaurant.dto.request.rating_restaurant;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRestaurantRequest {
    String comment;
    String resId;
    int ratePoint;
}
