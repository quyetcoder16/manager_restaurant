package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.rating_restaurant.RatingResUpdateRequest;
import com.promise.manager_restaurant.dto.request.rating_restaurant.RatingRestaurantRequest;
import com.promise.manager_restaurant.dto.response.rating_food.RatingFoodResponse;
import com.promise.manager_restaurant.dto.response.rating_restaurant.RatingRestaurantResponse;
import com.promise.manager_restaurant.entity.*;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.RatingRestaurantRepository;
import com.promise.manager_restaurant.repository.RestaurantRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.RatingRestaurantService;
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
public class RatingRestaurentServiceImpl implements RatingRestaurantService {

    UserRepository userRepository;

    RestaurantRepository restaurantRepository;

    RatingRestaurantRepository ratingRestaurantRepository;

    @Override
    public RatingRestaurantResponse createRatingRestaurant(RatingRestaurantRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

        if (existedRestaurant.getListOrder() == null){
            throw new AppException(ErrorCode.YOU_MUST_BUY_PRODUCT);
        }

        boolean checkUserBuy = false;

        for (Orders order: existedUser.getListOrder()){
            if (order.getRestaurant().getResID().equals(request.getResId())){
                checkUserBuy = true;
                break;
            }
        }
        if (!checkUserBuy){
            throw new AppException(ErrorCode.YOU_MUST_BUY_PRODUCT);
        }

        RatingRestaurant ratingRestaurant = RatingRestaurant.builder()
                .user(existedUser)
                .restaurant(existedRestaurant)
                .comment(request.getComment())
                .ratePoint(request.getRatePoint())
                .build();

        RatingRestaurant saved = ratingRestaurantRepository.save(ratingRestaurant);

        return RatingRestaurantResponse.builder()
                .ratingResId(saved.getRatingResId())
                .resId(request.getResId())
                .comment(saved.getComment())
                .ratePoint(saved.getRatePoint())
                .createdAt(saved.getCreatedAt())
                .userId(userId)
                .build();
    }

    @Override
    public RatingRestaurantResponse updateRatingRestaurant(RatingResUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        RatingRestaurant existedRatingRes = ratingRestaurantRepository.findById(request.getRatingResId())
                .orElseThrow(() -> new AppException(ErrorCode.RATINGRESTAURENT_NOT_EXISTED));

        if (!existedRatingRes.getUser().getUserId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getComment() != null){
            existedRatingRes.setComment(request.getComment());
        }

        if (request.getRatePoint() != 0){
            existedRatingRes.setRatePoint(request.getRatePoint());
        }

        RatingRestaurant saved = ratingRestaurantRepository.save(existedRatingRes);

        return RatingRestaurantResponse.builder()
                .ratingResId(saved.getRatingResId())
                .resId(saved.getRestaurant().getResID())
                .comment(saved.getComment())
                .ratePoint(saved.getRatePoint())
                .createdAt(saved.getCreatedAt())
                .userId(userId)
                .build();
    }


    @Override
    public void deleteRatingRestaurant(String request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        RatingRestaurant existedRatingRes = ratingRestaurantRepository.findById(request)
                .orElseThrow(() -> new AppException(ErrorCode.RATINGFOOD_NOT_EXISTED));

        if (existedRatingRes.getUser() != null){
            if (!existedRatingRes.getUser().getUserId().equals(userId)){
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        ratingRestaurantRepository.delete(existedRatingRes);
    }
}
