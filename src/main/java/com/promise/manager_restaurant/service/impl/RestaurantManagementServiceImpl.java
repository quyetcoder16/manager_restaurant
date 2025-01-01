package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.food.AddFoodRequest;
import com.promise.manager_restaurant.dto.request.food.DeleteFoodRequest;
import com.promise.manager_restaurant.dto.request.food.UpadateFoodRequest;
import com.promise.manager_restaurant.dto.request.restaurant.AddCategoryRequest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRemoveFromResquest;
import com.promise.manager_restaurant.dto.request.gallery.GalleryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.DeleteCategoryRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantCreationRequest;
import com.promise.manager_restaurant.dto.request.restaurant.RestaurantUpdateRequest;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.food.FoodResponse;
import com.promise.manager_restaurant.dto.response.gallery.GalleryResponse;
import com.promise.manager_restaurant.dto.response.restaurant.RestaurantResponse;
import com.promise.manager_restaurant.entity.*;
import com.promise.manager_restaurant.entity.keys.KeyFoodCategory;
import com.promise.manager_restaurant.entity.keys.KeyRestaurantCategory;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.FoodMapper;
import com.promise.manager_restaurant.mapper.RestaurantMapper;
import com.promise.manager_restaurant.repository.*;
import com.promise.manager_restaurant.service.FilesStorageService;
import com.promise.manager_restaurant.service.RestaurantManagementService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantManagementServiceImpl implements RestaurantManagementService {

    UserRepository userRepository;

    RestaurantRepository restaurantRepository;

    GalleryRepository galleryRepository;

    CategoryRepository categoryRepository;

    RestaurantCategoryRepository restaurantCategoryRepository;

    FoodRepository foodRepository;

    FoodCategoryRepository foodCategoryRepository;

    FilesStorageService filesStorageService;

    RestaurantMapper restaurantMapper;

    FoodMapper foodMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('READ_ALL_RESTAURANT','GET_LIST_YOUR_RESTAURANT')")
    public List<RestaurantResponse> getAlLRestaurants() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        List<Restaurant> restaurants = new ArrayList<>();

        if (isAdmin) {
            restaurants = restaurantRepository.findAll();
        } else {
            String managerId = authentication.getName();
            User manager = userRepository.findByUserId(managerId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            restaurants = restaurantRepository.findByManager(manager);
        }

        List<RestaurantResponse> restaurantResponses = new ArrayList<>();
        restaurants.forEach(restaurant -> restaurantResponses.add(restaurantMapper.tRestaurantResponse(restaurant)));
        return restaurantResponses;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RestaurantResponse createRestaurant(RestaurantCreationRequest request) {
        if (restaurantRepository.existsRestaurantByTitle(request.getTitle())) {
            throw new AppException(ErrorCode.TITLE_EXISTED);
        }

        if (restaurantRepository.existsRestaurantByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (restaurantRepository.existsRestaurantByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }


        Restaurant restaurant = restaurantMapper.tRestaurant(request);

        if (request.getBackground() != null) {
            String background = filesStorageService.saveFile(request.getBackground());
            restaurant.setBackground(background);
        }

        if (request.getLogo() != null) {
            String logo = filesStorageService.saveFile(request.getLogo());
            restaurant.setLogo(logo);
        }

        return restaurantMapper.tRestaurantResponse(restaurantRepository.save(restaurant));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public RestaurantResponse updateRestaurant(RestaurantUpdateRequest request, String id) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là manager khong
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        if (request.getTitle() != null) {
            if (!existingRestaurant.getTitle().equals(request.getTitle())) {
                if (restaurantRepository.existsRestaurantByTitle(request.getTitle())) {
                    throw new AppException(ErrorCode.TITLE_EXISTED);
                }
            }
            existingRestaurant.setTitle(request.getTitle());
        }

        if (request.getSubTitle() != null) {
            existingRestaurant.setSubTitle(request.getSubTitle());
        }

        if (request.getDescription() != null) {
            existingRestaurant.setDescription(request.getDescription());
        }

        if (request.getAddress() != null) {
            existingRestaurant.setAddress(request.getAddress());
        }


        if (request.getIsFreeship() != null) {
            existingRestaurant.setIsFreeship(Boolean.parseBoolean(request.getIsFreeship()));
        }

        if (request.getOpenDate() != null) {
            existingRestaurant.setOpenDate(LocalDate.parse(request.getOpenDate()));
        }

        if (request.getBackground() != null) {
            String background = filesStorageService.saveFile(request.getBackground());
            existingRestaurant.setBackground(background);
        }

        if (request.getLogo() != null) {
            String logo = filesStorageService.saveFile(request.getLogo());
            existingRestaurant.setLogo(logo);
        }

        if (request.getPhone() != null) {
            if (!existingRestaurant.getPhone().equals(request.getPhone())) {
                if (restaurantRepository.existsRestaurantByPhone(request.getPhone())) {
                    throw new AppException(ErrorCode.PHONE_EXISTED);
                }
                existingRestaurant.setPhone(request.getPhone());
            }
        }

        if (request.getEmail() != null) {
            if (!existingRestaurant.getEmail().equals(request.getEmail())) {
                if (restaurantRepository.existsRestaurantByEmail(request.getEmail())) {
                    throw new AppException(ErrorCode.EMAIL_EXISTED);
                }
            }
            existingRestaurant.setEmail(request.getEmail());
        }

        return restaurantMapper.tRestaurantResponse(restaurantRepository.save(existingRestaurant));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRestaurant(String id) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        restaurantRepository.delete(existingRestaurant);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public RestaurantResponse addGalleryToRestaurant(String id, GalleryRequest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là manager khong
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        if (request.getImage() != null) {
            String image = filesStorageService.saveFile(request.getImage());
            Gallery gallery = Gallery.builder()
                    .image(image)
                    .restaurant(existingRestaurant)
                    .build();
            galleryRepository.save(gallery);
            existingRestaurant.getListGallery().add(gallery);
        }
        return restaurantMapper.tRestaurantResponse(restaurantRepository.save(existingRestaurant));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('READ_ALL_RESTAURANT','GET_LIST_YOUR_RESTAURANT')")
    public List<GalleryResponse> getGalleries(String resId) {
        Restaurant existingRestaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        List<Gallery> galleries = new ArrayList<>();

        if (isAdmin) {
            galleries = galleryRepository.findByRestaurant(existingRestaurant);
        } else {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            galleries = galleryRepository.findByRestaurant(existingRestaurant);
        }

        List<GalleryResponse> galleriesResponse = new ArrayList<>();
        for (Gallery gallery : galleries) {
            GalleryResponse galleryResponse = GalleryResponse.builder()
                    .image(gallery.getImage())
                    .build();
            galleriesResponse.add(galleryResponse);
        }

        return galleriesResponse;
    }

    @Override
    public void deleteGalleryFromRestaurant(GalleryRemoveFromResquest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Gallery existingGallery = galleryRepository.findById(request.getGalleryId())
                .orElseThrow(() -> new AppException(ErrorCode.GALLERY_NOT_EXISTED));

        if (!existingRestaurant.getListGallery().contains(existingGallery)) {
            throw new AppException(ErrorCode.GALLERY_NOT_BELONG_RESTAURANT);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }
        galleryRepository.delete(existingGallery);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public void addCategoryToRestaurant(AddCategoryRequest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Category existingCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        //Kiểm tra thằng manager có sở hữu cái nhà hàng này không
        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        KeyRestaurantCategory keyRestaurantCategory = new KeyRestaurantCategory();
        keyRestaurantCategory.setResId(request.getResId());
        keyRestaurantCategory.setCateId(request.getCategoryId());

        if (restaurantCategoryRepository.existsById(keyRestaurantCategory)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED_IN_RESTAURANT);
        }

        restaurantCategoryRepository.save(RestaurantCategory.builder()
                .keyRestaurantCategory(keyRestaurantCategory)
                .build());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('READ_ALL_RESTAURANT','GET_LIST_YOUR_RESTAURANT')")
    public List<CategoryResponse> getCategories(String resId) {
        Restaurant existingRestaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        List<Category> categories = new ArrayList<>();

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findByRestaurant(existingRestaurant);
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        restaurantCategories.forEach(restaurantCategory -> {
            Category category = restaurantCategory.getCategory();
            categoryResponseList.add(CategoryResponse.builder()
                    .nameCategory(category.getNameCategory())
                    .build());
        });
        return categoryResponseList;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('READ_ALL_RESTAURANT','GET_LIST_YOUR_RESTAURANT')")
    public List<FoodResponse> getFoods(String resId) {
        Restaurant existingRestaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        List<FoodResponse> foodResponses = new ArrayList<>();
        existingRestaurant.getListFood().forEach(food -> {
            FoodResponse foodResponse = foodMapper.toFoodResponse(food);
            Category category = food.getListFoodCategory().get(0).getCategory();
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .nameCategory(category.getNameCategory())
                    .build();
            foodResponse.setCategory(categoryResponse);
            foodResponses.add(foodResponse);
        });
        return foodResponses;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public void deleteCategoryFromRestaurant(DeleteCategoryRequest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Category existingCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        KeyRestaurantCategory keyRestaurantCategory = new KeyRestaurantCategory();
        keyRestaurantCategory.setResId(request.getResId());
        keyRestaurantCategory.setCateId(request.getCategoryId());

        if (!restaurantCategoryRepository.existsById(keyRestaurantCategory)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED_IN_RESTAURANT);
        }

        restaurantCategoryRepository.deleteById(keyRestaurantCategory);
    }


    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public void addFoodToRestaurant(AddFoodRequest request, String resId) {
        Restaurant existingRestaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Category existingCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        Food food = foodMapper.toFood(request);
        if (request.getImage() != null) {
            String image = filesStorageService.saveFile(request.getImage());
            food.setImage(image);
        }

        food.setRestaurant(existingRestaurant);
        Food existingFood = foodRepository.save(food);

        KeyFoodCategory keyFoodCategory = new KeyFoodCategory();
        keyFoodCategory.setCategoryId(existingCategory.getCateID());
        keyFoodCategory.setFoodId(existingFood.getFoodId());

        if (foodCategoryRepository.existsById(keyFoodCategory)) {
            throw new AppException(ErrorCode.FOOD_IN_CATEGORY);
        }

        FoodCategory foodCategory = FoodCategory.builder()
                .keyFoodCategory(keyFoodCategory)
                .build();

        foodCategoryRepository.save(foodCategory);

        existingRestaurant.getListFood().add(food);
        restaurantRepository.save(existingRestaurant);

    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public void updateFoodOfRestaurant(UpadateFoodRequest request, String resId) {
        Restaurant existingRestaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        Food existingFood = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

        if (!existingRestaurant.getListFood().contains(existingFood)) {
            throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
        }

        if (request.getTitle() != null) {
            existingFood.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            existingFood.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            existingFood.setPrice(request.getPrice());
        }

        if (request.getTimeShip() != null) {
            existingFood.setTimeShip(request.getTimeShip());
        }

        if (request.getImage() != null) {
            String image = filesStorageService.saveFile(request.getImage());
            existingFood.setImage(image);
        }

        if (request.getCategoryId() != null) {
            Category existingCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            FoodCategory existingFoodCategory = existingFood.getListFoodCategory().get(0);
            if (existingFoodCategory != null){
                foodCategoryRepository.delete(existingFoodCategory);
                existingFood.getListFoodCategory().remove(existingFoodCategory);
            }

            KeyFoodCategory keyFoodCategory = new KeyFoodCategory();
            keyFoodCategory.setCategoryId(existingCategory.getCateID());
            keyFoodCategory.setFoodId(existingFood.getFoodId());

            if (foodCategoryRepository.existsById(keyFoodCategory)) {
                throw new AppException(ErrorCode.FOOD_IN_CATEGORY);
            }

            FoodCategory foodCategory = FoodCategory.builder()
                    .keyFoodCategory(keyFoodCategory)
                    .build();

            foodCategoryRepository.save(foodCategory);
            existingFood.getListFoodCategory().add(foodCategory);
        }

        foodRepository.save(existingFood);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('UPDATE_ALL_RESTAURANT', 'UPDATE_YOUR_RESTAURANT')")
    public void deleteFoodFromRestaurant(DeleteFoodRequest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách các vai trò của người dùng
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        // Kiểm tra vai trò xem là admin hay manager
        boolean isManager = (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER")));

        if (isManager) {
            String managerId = authentication.getName();
            if (existingRestaurant.getManager() == null
                    || existingRestaurant.getManager().getUserId() == null
                    || !existingRestaurant.getManager().getUserId().equals(managerId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        Food existingFood = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

        if (!existingRestaurant.getListFood().contains(existingFood)) {
            throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
        }

        foodRepository.delete(existingFood);
    }
}
