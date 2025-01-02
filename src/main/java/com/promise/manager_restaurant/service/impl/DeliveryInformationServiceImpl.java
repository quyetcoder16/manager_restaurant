package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.delivery_information.DeliveryInformationRequest;
import com.promise.manager_restaurant.dto.response.delivery_information.DeliveryInforResponse;
import com.promise.manager_restaurant.entity.DeliveryInformation;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.DeliveryInformationRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.DeliveryInformationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryInformationServiceImpl implements DeliveryInformationService {

    UserRepository userRepository;

    DeliveryInformationRepository deliveryInformationRepository;
    @Override
    public DeliveryInforResponse addDeliveryInformation(DeliveryInformationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User exitedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .user(exitedUser)
                .phone(request.getPhone())
                .address(request.getAddress())
                .fullName(request.getFullName())
                .build();

        DeliveryInformation saved = deliveryInformationRepository.save(deliveryInformation);
        return DeliveryInforResponse.builder()
                .deliveryId(saved.getDeliId())
                .address(saved.getAddress())
                .phone(saved.getPhone())
                .fullName(saved.getFullName())
                .build();
    }

    @Override
    public List<DeliveryInforResponse> getAllDeliveryInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        User exitedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (exitedUser.getListDeliveryInformation() == null) {
            return null;
        }
        List<DeliveryInformation> deliveryInformationList = exitedUser.getListDeliveryInformation();

        List<DeliveryInforResponse> responseList = deliveryInformationList.stream().map(deliveryInformation -> {

            return DeliveryInforResponse.builder()
                    .deliveryId(deliveryInformation.getDeliId())
                    .fullName(deliveryInformation.getFullName())
                    .phone(deliveryInformation.getPhone())
                    .address(deliveryInformation.getAddress())
                    .build();
        }).toList();

        return responseList;
    }
}
