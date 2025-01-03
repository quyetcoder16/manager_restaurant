package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.order_management.ChangeStatusOrderRequest;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import com.promise.manager_restaurant.dto.response.order_item.OrderItemResponse;
import com.promise.manager_restaurant.entity.OrderDetail;
import com.promise.manager_restaurant.entity.OrderStatus;
import com.promise.manager_restaurant.entity.Orders;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.OrderRepository;
import com.promise.manager_restaurant.repository.OrderStatusRepostiory;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.OrderManagementService;
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
public class OrderManagementServiceImpl implements OrderManagementService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    OrderStatusRepostiory orderStatusRepostiory;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrdersAdmin() {

        List<Orders> orders = orderRepository.findAll();

        List<OrderResponse> orderResponseList = orders.stream().map(order -> {
            List<OrderDetail> orderDetailList = new ArrayList<>();
            List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
            double total = 0;
            if (order.getListOrderDetail() != null) {
                orderDetailList = order.getListOrderDetail();
                for (OrderDetail orderDetail : orderDetailList) {
                    total += orderDetail.getFood().getPrice() * orderDetail.getQuantity();
                    orderItemResponseList.add(OrderItemResponse.builder()
                            .foodId(orderDetail.getFood().getFoodId())
                            .foodName(orderDetail.getFood().getTitle())
                            .quantity(orderDetail.getQuantity())
                            .price(orderDetail.getFood().getPrice())
                            .build());
                }
            }
            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .fullname(order.getDeliveryInformation().getFullName())
                    .phone(order.getDeliveryInformation().getPhone())
                    .address(order.getDeliveryInformation().getAddress())
                    .nameRestaurant(order.getRestaurant().getTitle())
                    .listOrderItems(orderItemResponseList)
                    .orderStatus(order.getOrderStatus().getOrderStatusId())
                    .totalPrice(total)
                    .build();
        }).toList();
        return orderResponseList;

    }

    @Override
    public List<OrderResponse> getAllOrdersManager() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String userId = authentication.getName();
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Orders> orders = new ArrayList<>();

        existedUser.getListRestaurant().forEach(restaurant -> {
            orders.addAll(restaurant.getListOrder());
        });

        List<OrderResponse> orderResponseList = orders.stream().map(order -> {
            List<OrderDetail> orderDetailList = new ArrayList<>();
            List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
            double total = 0;
            if (order.getListOrderDetail() != null) {
                orderDetailList = order.getListOrderDetail();
                for (OrderDetail orderDetail : orderDetailList) {
                    total += orderDetail.getFood().getPrice() * orderDetail.getQuantity();
                    orderItemResponseList.add(OrderItemResponse.builder()
                            .foodId(orderDetail.getFood().getFoodId())
                            .foodName(orderDetail.getFood().getTitle())
                            .quantity(orderDetail.getQuantity())
                            .price(orderDetail.getFood().getPrice())
                            .build());
                }
            }
            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .fullname(order.getDeliveryInformation().getFullName())
                    .phone(order.getDeliveryInformation().getPhone())
                    .address(order.getDeliveryInformation().getAddress())
                    .nameRestaurant(order.getRestaurant().getTitle())
                    .listOrderItems(orderItemResponseList)
                    .orderStatus(order.getOrderStatus().getOrderStatusId())
                    .totalPrice(total)
                    .build();
        }).toList();
        return orderResponseList;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse changeStatusOrderAdmin(ChangeStatusOrderRequest changeStatusOrderRequest) {

        Orders orders = orderRepository.findById(changeStatusOrderRequest.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED)
        );

        double total = 0;
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        if (orders.getListOrderDetail() != null) {
            for (OrderDetail orderDetail : orders.getListOrderDetail()) {
                total += orderDetail.getFood().getPrice() * orderDetail.getQuantity();
                orderItemResponseList.add(
                        OrderItemResponse.builder()
                                .foodId(orderDetail.getFood().getFoodId())
                                .foodName(orderDetail.getFood().getTitle())
                                .quantity(orderDetail.getQuantity())
                                .price(orderDetail.getFood().getPrice())
                                .build()
                );
            }
        }

        OrderStatus orderStatus = orderStatusRepostiory.findById(changeStatusOrderRequest.getStatusId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_STATUS_NOT_EXISTED)
        );

        orders.setOrderStatus(orderStatus);
        orderRepository.save(orders);

        return OrderResponse.builder()
                .orderId(orders.getOrderId())
                .fullname(orders.getDeliveryInformation().getFullName())
                .phone(orders.getDeliveryInformation().getPhone())
                .address(orders.getDeliveryInformation().getAddress())
                .nameRestaurant(orders.getRestaurant().getTitle())
                .listOrderItems(orderItemResponseList)
                .orderStatus(orderStatus.getOrderStatusId())
                .totalPrice(total)
                .build();
    }

    @Override
    public OrderResponse changeStatusOrderManagement(ChangeStatusOrderRequest changeStatusOrderRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String userId = authentication.getName();

        Orders orders = orderRepository.findById(changeStatusOrderRequest.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED)
        );

        if (!orders.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        double total = 0;
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        if (orders.getListOrderDetail() != null) {
            for (OrderDetail orderDetail : orders.getListOrderDetail()) {
                total += orderDetail.getFood().getPrice() * orderDetail.getQuantity();
                orderItemResponseList.add(
                        OrderItemResponse.builder()
                                .foodId(orderDetail.getFood().getFoodId())
                                .foodName(orderDetail.getFood().getTitle())
                                .quantity(orderDetail.getQuantity())
                                .price(orderDetail.getFood().getPrice())
                                .build()
                );
            }
        }

        OrderStatus orderStatus = orderStatusRepostiory.findById(changeStatusOrderRequest.getStatusId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_STATUS_NOT_EXISTED)
        );

        orders.setOrderStatus(orderStatus);
        orderRepository.save(orders);

        return OrderResponse.builder()
                .orderId(orders.getOrderId())
                .fullname(orders.getDeliveryInformation().getFullName())
                .phone(orders.getDeliveryInformation().getPhone())
                .address(orders.getDeliveryInformation().getAddress())
                .nameRestaurant(orders.getRestaurant().getTitle())
                .listOrderItems(orderItemResponseList)
                .orderStatus(orderStatus.getOrderStatusId())
                .totalPrice(total)
                .build();
    }
}
