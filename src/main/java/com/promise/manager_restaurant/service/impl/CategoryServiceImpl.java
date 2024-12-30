package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.entity.Category;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.CategoryMapper;
import com.promise.manager_restaurant.repository.CategoryRepository;
import com.promise.manager_restaurant.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsCategoriesByNameCategory(request.getNameCategory())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = categoryMapper.tCategory(request);

        return categoryMapper.tCateResponse(categoryRepository.save(category));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(CategoryRequest request, String id) {

        if (categoryRepository.existsCategoriesByNameCategory(request.getNameCategory())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        category.setNameCategory(request.getNameCategory());

        return categoryMapper.tCateResponse(categoryRepository.save(category));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categoryRepository.findAll()
                .forEach(category -> categoryResponses.add(categoryMapper.tCateResponse(category)));
        return categoryResponses;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        categoryRepository.deleteById(id);
    }

}
