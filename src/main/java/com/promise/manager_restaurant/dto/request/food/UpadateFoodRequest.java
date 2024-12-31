package com.promise.manager_restaurant.dto.request.food;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpadateFoodRequest {

    String foodId;

    String title;

    String description;

    MultipartFile image;

    String timeShip;

    @DecimalMin(value = "0.0001", message = "INVALID_PRICE")
    Double price;
}
