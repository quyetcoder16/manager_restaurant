package com.promise.manager_restaurant.dto.response.order_item;

import com.promise.manager_restaurant.dto.response.food.FoodResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    String foodId;
    String foodName;
    Integer quantity;
    double price;
    LocalDateTime createDate;
}
