package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public CategoryResponse createCategory(CategoryRequest request);
    public CategoryResponse updateCategory(CategoryRequest request, String id);
    public List<CategoryResponse> getAllCategories();
    void deleteCategory(String id);
}
