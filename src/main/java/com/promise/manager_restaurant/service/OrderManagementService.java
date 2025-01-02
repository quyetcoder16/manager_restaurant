package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.order_management.ChangeStatusOrderRequest;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderManagementService {
    public List<OrderResponse> getAllOrdersAdmin();

    public List<OrderResponse> getAllOrdersManager();

    public OrderResponse changeStatusOrderAdmin(ChangeStatusOrderRequest changeStatusOrderRequest);

    public OrderResponse changeStatusOrderManagement(ChangeStatusOrderRequest changeStatusOrderRequest);

}
