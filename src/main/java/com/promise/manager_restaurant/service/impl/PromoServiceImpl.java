package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.promo.PromoRequest;
import com.promise.manager_restaurant.dto.request.promo.PromoUpdateRequest;
import com.promise.manager_restaurant.dto.response.promo.PromoReponse;
import com.promise.manager_restaurant.entity.Food;
import com.promise.manager_restaurant.entity.Promo;
import com.promise.manager_restaurant.entity.Restaurant;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.FoodRepository;
import com.promise.manager_restaurant.repository.PromoRepository;
import com.promise.manager_restaurant.repository.RestaurantRepository;
import com.promise.manager_restaurant.service.PromoService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromoServiceImpl implements PromoService {

    RestaurantRepository restaurantRepository;

    FoodRepository foodRepository;

    PromoRepository promoRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PromoReponse createPromo(PromoRequest request) {
        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        List<Food> listAppliedFood = new ArrayList<>();

        for (String foodId : request.getListFoods()){
            Food existedFood = foodRepository.findById(foodId)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));
            if (!existedRestaurant.getListFood().contains(existedFood)){
                throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
            }
            listAppliedFood.add(existedFood);
        }

        Promo newPromo = Promo.builder()
                .restaurant(existedRestaurant)
                .percent(request.getPercent())
                .listFoods(listAppliedFood)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Promo saved = promoRepository.save(newPromo);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return  PromoReponse.builder()
                .promoId(saved.getPromoId())
                .resId(saved.getRestaurant().getResID())
                .percent(saved.getPercent())
                .foodIds(request.getListFoods())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .build();

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PromoReponse updatePromo(PromoUpdateRequest request) {
        Promo existedPromod = promoRepository.findById(request.getPromoId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        if (request.getResId() == null) {
            request.setResId(existedPromod.getRestaurant().getResID());
        }

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        existedPromod.getListFoods().forEach(food -> food.setPromo(null));

        if (request.getPercent() != null) {
            existedPromod.setPercent(request.getPercent());
        }

        if (request.getStartDate() != null) {
            existedPromod.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            existedPromod.setEndDate(request.getEndDate());
        }

        if (request.getListFoods() != null && !request.getListFoods().isEmpty()) {
            // Xóa liên kết cũ giữa Promo và các Food
            existedPromod.getListFoods().forEach(food -> food.setPromo(null));
            existedPromod.getListFoods().clear();

            // Tạo danh sách mới
            List<Food> listAppliedFood = new ArrayList<>();
            for (String foodId : request.getListFoods()) {
                Food existedFood = foodRepository.findById(foodId)
                        .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));
                if (!existedRestaurant.getListFood().contains(existedFood)) {
                    throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
                }
                listAppliedFood.add(existedFood);
            }
            existedPromod.setListFoods(listAppliedFood);
            listAppliedFood.forEach(food -> food.setPromo(existedPromod));
        }
        Promo saved = promoRepository.save(existedPromod);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return  PromoReponse.builder()
                .promoId(saved.getPromoId())
                .resId(saved.getRestaurant().getResID())
                .percent(saved.getPercent())
                .foodIds(request.getListFoods())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .build();

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePromo(String requestId) {
        Promo existedPromod = promoRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        existedPromod.getListFoods().forEach(food -> food.setPromo(null));
        promoRepository.delete(existedPromod);
    }
}
