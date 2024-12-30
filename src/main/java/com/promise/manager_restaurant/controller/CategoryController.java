package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.category.CategoryRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{cateId}")
    ApiResponse<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable("cateId") String id){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(request,id))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryResponse>> getAllCategories(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @DeleteMapping("/{cateId}")
    ApiResponse<Void> deleteCategory(@PathVariable("cateId") String id){
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .message("Remove category successfully")
                .build();
    }
}
