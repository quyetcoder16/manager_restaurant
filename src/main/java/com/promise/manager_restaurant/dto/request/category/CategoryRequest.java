package com.promise.manager_restaurant.dto.request.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
   String nameCategory;
}
