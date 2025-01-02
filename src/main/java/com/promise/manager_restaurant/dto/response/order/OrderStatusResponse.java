package com.promise.manager_restaurant.dto.response.order;


import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {

    String orderStatusId;

    String nameStatus;

    String description;
}
