package com.promise.manager_restaurant.dto.request.delivery_information;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryInformationRequest {

    String fullName;

    String phone;

    String address;
}