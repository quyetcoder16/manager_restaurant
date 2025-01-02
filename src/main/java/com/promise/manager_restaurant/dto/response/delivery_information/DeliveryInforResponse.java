package com.promise.manager_restaurant.dto.response.delivery_information;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryInforResponse {

    String deliveryId;
    String fullName;
    String phone;
    String address;
}
