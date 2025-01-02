package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.oder_item.OrderItemRequest;
import com.promise.manager_restaurant.dto.request.order.OrderCreationRequest;

import com.promise.manager_restaurant.dto.request.order.OrderUpdateRequest;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import com.promise.manager_restaurant.dto.response.order.OrderStatusResponse;
import com.promise.manager_restaurant.dto.response.order_item.OrderItemResponse;
import com.promise.manager_restaurant.entity.*;
import com.promise.manager_restaurant.entity.keys.KeyOrderDetail;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.repository.*;
import com.promise.manager_restaurant.service.OrderService;
import com.promise.manager_restaurant.utils.Const;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    UserRepository userRepository;

    RestaurantRepository restaurantRepository;

    OrderItemRepository orderItemRepository;

    DeliveryInformationRepository deliveryInformationRepository;

    OrderStatusRepostiory orderStatusRepostiory;

    //OrderItemService orderItemService;
    private final FoodRepository foodRepository;

    @Override

    public void createOrder(OrderCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.getName().equals(request.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User existedUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        DeliveryInformation existedDeliveryInformation = deliveryInformationRepository.findById(request.getDeliveryInforId())
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_EXISTED));

        if (existedUser.getListDeliveryInformation() == null || !existedUser.getListDeliveryInformation().contains(existedDeliveryInformation)) {
            throw new AppException(ErrorCode.DELIVERY_NOT_EXISTED);
        }

        if (request.getListFood() == null || request.getListFood().size() == 0) {
            throw new AppException(ErrorCode.LIST_FOOD_IS_EMPTY);
        }

        OrderStatus orderStatus = orderStatusRepostiory.findById(Const.ORDER_STATUS.MOI_LEN_DON.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));

        Orders newOrder = Orders.builder()
                .user(existedUser)
                .restaurant(existedRestaurant)
                .orderStatus(orderStatus)
                .deliveryInformation(existedDeliveryInformation)
                .deliveryInformation(existedDeliveryInformation)
                .build();

        Orders existedOrder = orderRepository.save(newOrder);

        Map<String, Integer> listFood = new HashMap<>();
        request.getListFood().forEach(orderItemRequest -> {
            listFood.put(orderItemRequest.getFoodId(), orderItemRequest.getQuantity());
        });

        for (String foodId : listFood.keySet()) {
            Food existedFood = foodRepository.findById(foodId)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

            if (existedRestaurant.getListFood() == null || !existedRestaurant.getListFood().contains(existedFood)) {
                throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
            }

            KeyOrderDetail keyOrderDetail = new KeyOrderDetail();
            keyOrderDetail.setFoodId(foodId);
            keyOrderDetail.setOrderId(existedOrder.getOrderId());

            OrderDetail existedOrderDetail = orderItemRepository.findById(keyOrderDetail).orElse(null);

            if (existedOrderDetail != null) {
                existedOrderDetail.setQuantity(existedOrderDetail.getQuantity() + listFood.get(foodId));
                orderItemRepository.save(existedOrderDetail);
            } else {
                OrderDetail newOrderDetail = OrderDetail.builder()
                        .keyOrderDetail(keyOrderDetail)
                        .quantity(listFood.get(foodId))
                        .build();
                orderItemRepository.save(newOrderDetail);
            }
        }
    }

    @Override
    public void updateOrder(OrderUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.getName().equals(request.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Orders existedOrder = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        User existedUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Restaurant existedRestaurant = restaurantRepository.findById(request.getResId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        DeliveryInformation existedDeliveryInformation = deliveryInformationRepository.findById(request.getDeliveryInforId())
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_EXISTED));

        if (request.getListFood() == null || request.getListFood().size() == 0) {
            throw new AppException(ErrorCode.LIST_FOOD_IS_EMPTY);
        }


        if (existedUser.getListDeliveryInformation() == null
                || !existedUser.getListDeliveryInformation().contains(existedDeliveryInformation)) {
            throw new AppException(ErrorCode.DELIVERY_NOT_EXISTED);
        }

        if (existedOrder.getOrderStatus() == null || !existedOrder.getOrderStatus().getOrderStatusId().equals(Const.ORDER_STATUS.MOI_LEN_DON.getId())) {
            throw new AppException(ErrorCode.CAN_NOT_UPDATE_ORDER);
        }
        ;

        existedOrder.setDeliveryInformation(existedDeliveryInformation);

        orderRepository.save(existedOrder);

        int numberOfItems = orderItemRepository.deleteAllByOrder(existedOrder);

        Map<String, Integer> listFood = new HashMap<>();
        request.getListFood().forEach(orderItemRequest -> {
            listFood.put(orderItemRequest.getFoodId(), orderItemRequest.getQuantity());
        });

        for (String foodId : listFood.keySet()) {
            Food existedFood = foodRepository.findById(foodId)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_EXISTED));

            if (existedRestaurant.getListFood() == null
                    || !existedRestaurant.getListFood().contains(existedFood)) {
                throw new AppException(ErrorCode.FOOD_NOT_IN_RESTAURANT);
            }

            KeyOrderDetail keyOrderDetail = new KeyOrderDetail();
            keyOrderDetail.setFoodId(foodId);
            keyOrderDetail.setOrderId(existedOrder.getOrderId());

            OrderDetail existedOrderDetail = orderItemRepository.findById(keyOrderDetail).orElse(null);

            if (existedOrderDetail != null) {
                existedOrderDetail.setQuantity(existedOrderDetail.getQuantity() + listFood.get(foodId));
                orderItemRepository.save(existedOrderDetail);
            } else {
                OrderDetail newOrderDetail = OrderDetail.builder()
                        .keyOrderDetail(keyOrderDetail)
                        .quantity(listFood.get(foodId))
                        .build();
                orderItemRepository.save(newOrderDetail);
            }
        }
    }

    @Override
    public List<OrderResponse> getAllOrderOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String userId = authentication.getName();
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<OrderResponse> orderResponseList = existedUser.getListOrder().stream().map(orders -> {
            List<OrderDetail> orderDetailList = new ArrayList<>();
            List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
            double total = 0;
            if (orders.getListOrderDetail() != null) {
                orderDetailList = orders.getListOrderDetail();
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
                    .orderId(orders.getOrderId())
                    .fullname(orders.getDeliveryInformation().getFullName())
                    .phone(orders.getDeliveryInformation().getPhone())
                    .address(orders.getDeliveryInformation().getAddress())
                    .nameRestaurant(orders.getRestaurant().getTitle())
                    .listOrderItems(orderItemResponseList)
                    .totalPrice(total)
                    .build();
        }).toList();
        return orderResponseList;
    }


    @Override
    public List<OrderStatusResponse> getAllOrderStatus() {
        List<OrderStatusResponse> orderStatusResponseList = orderStatusRepostiory.findAll().stream().map(orderStatus ->
        {
            return OrderStatusResponse.builder()
                    .orderStatusId(orderStatus.getOrderStatusId())
                    .nameStatus(orderStatus.getNameStatus())
                    .description(orderStatus.getDescription())
                    .build();
        }).toList();
        return orderStatusResponseList;
    }
}
