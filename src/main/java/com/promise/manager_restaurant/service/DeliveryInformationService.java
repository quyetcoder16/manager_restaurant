package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.delivery_information.DeliveryInforUpdateRequest;
import com.promise.manager_restaurant.dto.request.delivery_information.DeliveryInformationRequest;
import com.promise.manager_restaurant.dto.response.delivery_information.DeliveryInforResponse;
import com.promise.manager_restaurant.entity.DeliveryInformation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryInformationService {
    public DeliveryInforResponse addDeliveryInformation(DeliveryInformationRequest deliveryInformation);
    public DeliveryInforResponse updateDeliveryInformation(DeliveryInforUpdateRequest deliveryInformation);
    public void deleteDeliveryInformation(String requestId);
    public List<DeliveryInforResponse> getAllDeliveryInformation();
}
