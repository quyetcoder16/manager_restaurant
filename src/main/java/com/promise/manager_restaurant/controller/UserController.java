package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.delivery_information.DeliveryInforUpdateRequest;
import com.promise.manager_restaurant.dto.request.delivery_information.DeliveryInformationRequest;
import com.promise.manager_restaurant.dto.request.user.ChangePasswordUserRequest;
import com.promise.manager_restaurant.dto.request.user.UserUpdateRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.delivery_information.DeliveryInforResponse;
import com.promise.manager_restaurant.dto.response.order.OrderResponse;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.service.DeliveryInformationService;
import com.promise.manager_restaurant.service.FilesStorageService;
import com.promise.manager_restaurant.service.OrderService;
import com.promise.manager_restaurant.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    DeliveryInformationService deliveryInformationService;
    OrderService orderService;
    FilesStorageService filesStorageService;


    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileRestaurant(@PathVariable String filename) {
        Resource resource = filesStorageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/update_info")
    public ResponseEntity<ApiResponse<UserResponse>> updateInfo(@ModelAttribute UserUpdateRequest request) {
        return new ResponseEntity<>(ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(request))
                .message("User updated successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/change_password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordUserRequest changePasswordUserRequest) {
        userService.changePassword(changePasswordUserRequest);
        return new ResponseEntity<>(ApiResponse.<Void>builder()
                .message("Password changed successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/deliveries")
    public ApiResponse<DeliveryInforResponse> addDeliveryInformation(@RequestBody @Valid DeliveryInformationRequest request){
        return ApiResponse.<DeliveryInforResponse>builder()
                .data(deliveryInformationService.addDeliveryInformation(request))
                .message("Delivery added successfully!")
                .build();
    }

    @PutMapping("/deliveries")
    public ApiResponse<DeliveryInforResponse> updateDeliveryInformation(@RequestBody @Valid DeliveryInforUpdateRequest request){
        return ApiResponse.<DeliveryInforResponse>builder()
                .data(deliveryInformationService.updateDeliveryInformation(request))
                .message("Delivery added successfully!")
                .build();
    }

    @DeleteMapping("deliveries/{deliId}")
    public ApiResponse<Void> deleteDeliveryInformation(@PathVariable String deliId){
        deliveryInformationService.deleteDeliveryInformation(deliId);
        return ApiResponse.<Void>builder()
                .message("Delivery deleted successfully!")
                .build();
    }

    @GetMapping("/deliveries")
    public ApiResponse<List<DeliveryInforResponse>> getAllDeliveryInformation(){
        return ApiResponse.<List<DeliveryInforResponse>>builder()
                .data(deliveryInformationService.getAllDeliveryInformation())
                .message("Delivery list successfully!")
                .build();
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> getAllOrders(){
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getAllOrderOfUser())
                .message("Order list successfully!")
                .build();
    }
}
