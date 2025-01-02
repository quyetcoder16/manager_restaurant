package com.promise.manager_restaurant.dto.request.order_management;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatusOrderRequest {
    String orderId;
    String statusId;
}
