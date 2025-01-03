package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.rating_food.RatingFoodRequest;
import com.promise.manager_restaurant.dto.response.rating_food.RatingFoodResponse;
import com.promise.manager_restaurant.entity.*;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.FoodRepository;
import com.promise.manager_restaurant.repository.RatingFoodRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.RatingFoodService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingFoodServiceImpl implements RatingFoodService {

    RatingFoodRepository ratingFoodRepository;

    UserRepository userRepository;

    FoodRepository foodRepository;
    @Override
    public RatingFoodResponse createRatingFood(RatingFoodRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Food existedFood = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

        if (existedFood.getListOrderDetail() == null){
            throw new AppException(ErrorCode.YOU_MUST_BUY_PRODUCT);
        }

        boolean checkUserBuy = false;

        for (OrderDetail orderDetail : existedFood.getListOrderDetail()){
            Orders order = orderDetail.getOrder();
            if (order.getUser().getUserId().equals(userId)){
                checkUserBuy = true;
                break;
            }
        }
        if (!checkUserBuy){
            throw new AppException(ErrorCode.YOU_MUST_BUY_PRODUCT);
        }

        RatingFood ratingFood = RatingFood.builder()
                .user(existedUser)
                .food(existedFood)
                .comment(request.getComment())
                .ratePoint(request.getRatePoint())
                .build();

        RatingFood saved = ratingFoodRepository.save(ratingFood);

        return RatingFoodResponse.builder()
                .ratingFoodId(saved.getRatingFoodId())
                .foodId(request.getFoodId())
                .comment(saved.getComment())
                .ratePoint(saved.getRatePoint())
                .createdAt(saved.getCreatedAt())
                .userId(userId)
                .build();
    }

    @Override
    public void deleteRatingFood(String request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        RatingFood existedRatingFood = ratingFoodRepository.findById(request)
                .orElseThrow(() -> new AppException(ErrorCode.RATINGFOOD_NOT_EXISTED));

        if (existedRatingFood.getUser() != null){
            if (!existedRatingFood.getUser().getUserId().equals(userId)){
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        ratingFoodRepository.delete(existedRatingFood);
    }
}
