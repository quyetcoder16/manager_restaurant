package com.promise.manager_restaurant.dto.request.restaurant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCategoryRequest {
    String resId;
    String categoryId;
}
