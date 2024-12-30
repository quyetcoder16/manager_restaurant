package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category tCategory(CategoryRequest request);

    CategoryResponse tCateResponse(Category category);

}
