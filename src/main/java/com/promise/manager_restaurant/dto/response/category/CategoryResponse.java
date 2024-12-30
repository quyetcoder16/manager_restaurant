package com.promise.manager_restaurant.dto.response.category;

import com.promise.manager_restaurant.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse{
    private String nameCategory;
}
