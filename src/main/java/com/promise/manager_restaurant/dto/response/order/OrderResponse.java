package com.promise.manager_restaurant.dto.response.order;

import com.promise.manager_restaurant.dto.response.order_item.OrderItemResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    String orderId;

    String fullname;

    String phone;

    String address;

    String nameRestaurant;

    List<OrderItemResponse> listOrderItems;

    Double totalPrice;
}
