package com.promise.manager_restaurant.dto.request.restaurant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteCategoryRequest {
    String resId;
    String categoryId;
}
