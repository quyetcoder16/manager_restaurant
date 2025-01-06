package com.promise.manager_restaurant.dto.request.rating_restaurant;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResUpdateRequest {
    String ratingResId;

    String comment;

    int ratePoint;
}
