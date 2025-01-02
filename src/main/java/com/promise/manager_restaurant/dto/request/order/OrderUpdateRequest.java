package com.promise.manager_restaurant.dto.request.order;

import com.promise.manager_restaurant.dto.request.oder_item.OrderItemRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    String orderId;
    String resId;
    String userId;
    String deliveryInforId;
    List<OrderItemRequest> listFood;
}
