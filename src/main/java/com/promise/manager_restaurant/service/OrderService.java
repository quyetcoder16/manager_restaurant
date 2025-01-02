package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.order.OrderCreationRequest;
import com.promise.manager_restaurant.dto.request.order.OrderUpdateRequest;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import com.promise.manager_restaurant.dto.response.order.OrderStatusResponse;
import com.promise.manager_restaurant.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public void createOrder(OrderCreationRequest request);

    public void updateOrder(OrderUpdateRequest request);

    public List<OrderResponse> getAllOrderOfUser();

    public List<OrderStatusResponse> getAllOrderStatus();
}
