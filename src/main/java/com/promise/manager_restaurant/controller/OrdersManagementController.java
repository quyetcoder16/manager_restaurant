package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.order_management.ChangeStatusOrderRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import com.promise.manager_restaurant.service.OrderManagementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order_management")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersManagementController {

    OrderManagementService orderManagementService;

    @GetMapping("/get_all_order")
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderManagementService.getAllOrdersAdmin())
                .message("Order list successfully!")
                .build();
    }

    @GetMapping("/get_all_order_manager")
    public ApiResponse<List<OrderResponse>> getAllOrdersManager() {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderManagementService.getAllOrdersManager())
                .message("Order list successfully!")
                .build();
    }

    @PutMapping("/change_status_order_admin")
    public ApiResponse<OrderResponse> changeStatusOrderAdmin(@RequestBody ChangeStatusOrderRequest changeStatusOrderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderManagementService.changeStatusOrderAdmin(changeStatusOrderRequest))
                .message("Status Order successfully changed!")
                .build();
    }

    @PutMapping("/change_status_order_management")
    public ApiResponse<OrderResponse> changeStatusOrderManagement(@RequestBody ChangeStatusOrderRequest changeStatusOrderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderManagementService.changeStatusOrderManagement(changeStatusOrderRequest))
                .message("Status Order successfully changed!")
                .build();
    }

}
