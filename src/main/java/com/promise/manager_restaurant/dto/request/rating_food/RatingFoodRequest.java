package com.promise.manager_restaurant.dto.request.rating_food;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingFoodRequest {
    @NotBlank
    String comment;

    @NotBlank
    String foodId;

    @NotBlank
    String orderId;

    @Min(value = 1)
            @Max(value = 5)
    int ratePoint;
}
