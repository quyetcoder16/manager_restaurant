package com.promise.manager_restaurant.dto.request.oder_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {

    @NotNull
    String foodId;

    @NotNull
    @Min(value = 1, message = "QUANTITY_EQUAL_OR_MORE_1")
    Integer quantity;

}
