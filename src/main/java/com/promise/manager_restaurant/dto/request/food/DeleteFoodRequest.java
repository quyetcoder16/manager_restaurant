package com.promise.manager_restaurant.dto.request.food;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteFoodRequest {
    String foodId;
    String resId;
}
