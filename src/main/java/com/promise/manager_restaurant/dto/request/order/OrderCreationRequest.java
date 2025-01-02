package com.promise.manager_restaurant.dto.request.order;

import com.promise.manager_restaurant.dto.request.oder_item.OrderItemRequest;
import com.promise.manager_restaurant.entity.Food;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {

    String resId;
    String userId;
    String deliveryInforId;
    List<OrderItemRequest> listFood;
}
