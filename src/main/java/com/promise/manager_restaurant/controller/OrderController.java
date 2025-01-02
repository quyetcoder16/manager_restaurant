package com.promise.manager_restaurant.controller;


import com.promise.manager_restaurant.dto.request.order.OrderCreationRequest;
import com.promise.manager_restaurant.dto.request.order.OrderUpdateRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    ApiResponse<Void> createOrder(@RequestBody OrderCreationRequest request){
        orderService.createOrder(request);
        return ApiResponse.<Void>builder()
                .message("Create order successfully")
                .build();
    }

    @PutMapping
    ApiResponse<Void> createOrder(@RequestBody OrderUpdateRequest request){
        orderService.updateOrder(request);
        return ApiResponse.<Void>builder()
                .message("Update order successfully")
                .build();
    }
}
