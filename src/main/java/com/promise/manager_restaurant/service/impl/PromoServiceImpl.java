package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.promo.PromoRequest;
import com.promise.manager_restaurant.dto.request.promo.PromoUpdateRequest;
import com.promise.manager_restaurant.dto.response.promo.PromoReponse;
import com.promise.manager_restaurant.entity.Food;
import com.promise.manager_restaurant.entity.Promo;
import com.promise.manager_restaurant.entity.Restaurant;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.FoodRepository;
import com.promise.manager_restaurant.repository.PromoRepository;
import com.promise.manager_restaurant.repository.RestaurantRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.PromoService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromoServiceImpl implements PromoService {

    RestaurantRepository restaurantRepository;

    UserRepository userRepository;

    FoodRepository foodRepository;

    PromoRepository promoRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PromoReponse createPromo(PromoRequest request) {
        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        if (request.getPercent() <= 0 || request.getPercent() > 100) {
            throw new AppException(ErrorCode.PROMO_INVALID);
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.DATE_PROMO_INVALID);
        }

        if (promoRepository.existsPromoByPromoCode(request.getPromoCode())) {
            throw new AppException(ErrorCode.PROMO_EXISTED);
        }

        List<Food> listAppliedFood = new ArrayList<>();

        for (String foodId : request.getListFoods()) {
            Food existedFood = foodRepository.findById(foodId)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));
            if (!existedRestaurant.getListFood().contains(existedFood)) {
                throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
            }
            listAppliedFood.add(existedFood);
        }


        Promo newPromo = Promo.builder()
                .restaurant(existedRestaurant)
                .percent(request.getPercent())
                .promoCode(request.getPromoCode())
                .listFoods(listAppliedFood)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Promo saved = promoRepository.save(newPromo);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return PromoReponse.builder()
                .promoId(saved.getPromoId())
                .promoCode(saved.getPromoCode())
                .resId(saved.getRestaurant().getResID())
                .percent(saved.getPercent())
                .foodIds(request.getListFoods())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .build();

    }

    @Override
    public PromoReponse createPromoManagement(PromoRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHORIZED));

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        if (!user.getListRestaurant().contains(existedRestaurant)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getPercent() <= 0 || request.getPercent() > 100) {
            throw new AppException(ErrorCode.PROMO_INVALID);
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.DATE_PROMO_INVALID);
        }

        if (promoRepository.existsPromoByPromoCode(request.getPromoCode())) {
            throw new AppException(ErrorCode.PROMO_EXISTED);
        }

        List<Food> listAppliedFood = new ArrayList<>();

        for (String foodId : request.getListFoods()) {
            Food existedFood = foodRepository.findById(foodId)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));
            if (!existedRestaurant.getListFood().contains(existedFood)) {
                throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
            }
            listAppliedFood.add(existedFood);
        }


        Promo newPromo = Promo.builder()
                .restaurant(existedRestaurant)
                .percent(request.getPercent())
                .promoCode(request.getPromoCode())
                .listFoods(listAppliedFood)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Promo saved = promoRepository.save(newPromo);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return PromoReponse.builder()
                .promoId(saved.getPromoId())
                .promoCode(saved.getPromoCode())
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

        if (request.getPercent() <= 0 || request.getPercent() > 100) {
            throw new AppException(ErrorCode.PROMO_INVALID);
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.DATE_PROMO_INVALID);
        }

        if (promoRepository.existsPromoByPromoCode(request.getPromoCode())) {
            Promo promoExistByCode = promoRepository.findByPromoCode(request.getPromoCode());
            if (!promoExistByCode.getPromoId().equals(request.getPromoId())) {
                throw new AppException(ErrorCode.PROMO_EXISTED);
            }
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
        existedPromod.setPromoCode(request.getPromoCode());
        Promo saved = promoRepository.save(existedPromod);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return PromoReponse.builder()
                .promoId(saved.getPromoId())
                .resId(saved.getRestaurant().getResID())
                .percent(saved.getPercent())
                .promoCode(saved.getPromoCode())
                .foodIds(request.getListFoods())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .build();

    }

    @Override
    public PromoReponse updatePromoManagement(PromoUpdateRequest request) {
        Promo existedPromod = promoRepository.findById(request.getPromoId())
                .orElseThrow(() -> new AppException(ErrorCode.PROMO_NOT_EXISTED));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHORIZED));

        if (request.getResId() == null) {
            request.setResId(existedPromod.getRestaurant().getResID());
        }

        if (request.getPercent() <= 0 || request.getPercent() > 100) {
            throw new AppException(ErrorCode.PROMO_INVALID);
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.DATE_PROMO_INVALID);
        }

        if (promoRepository.existsPromoByPromoCode(request.getPromoCode())) {
            Promo promoExistByCode = promoRepository.findByPromoCode(request.getPromoCode());
            if (!promoExistByCode.getPromoId().equals(request.getPromoId())) {
                throw new AppException(ErrorCode.PROMO_EXISTED);
            }
        }

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        if (!user.getListRestaurant().contains(existedRestaurant)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

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
        existedPromod.setPromoCode(request.getPromoCode());
        Promo saved = promoRepository.save(existedPromod);

        saved.getListFoods().forEach(food -> food.setPromo(saved));

        return PromoReponse.builder()
                .promoId(saved.getPromoId())
                .resId(saved.getRestaurant().getResID())
                .percent(saved.getPercent())
                .promoCode(saved.getPromoCode())
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

    @Override
    public void deletePromoManagement(String requestId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHORIZED));

        Promo existedPromod = promoRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        if (!user.getListRestaurant().contains(existedPromod.getRestaurant())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        existedPromod.getListFoods().forEach(food -> food.setPromo(null));
        promoRepository.delete(existedPromod);
    }
}
