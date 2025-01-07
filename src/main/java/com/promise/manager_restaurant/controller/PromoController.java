package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.promo.PromoRequest;
import com.promise.manager_restaurant.dto.request.promo.PromoUpdateRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.promo.PromoReponse;
import com.promise.manager_restaurant.service.PromoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promos")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromoController {

    PromoService promoService;

    @PostMapping
    public ApiResponse<PromoReponse> createPromo(@RequestBody PromoRequest request) {
        return ApiResponse.<PromoReponse>builder()
                .data(promoService.createPromo(request))
                .message("Create Promo successfully")
                .build();
    }

    @PutMapping
    public ApiResponse<PromoReponse> updatePromod(@RequestBody PromoUpdateRequest request) {
        return ApiResponse.<PromoReponse>builder()
                .data(promoService.updatePromo(request))
                .message("update Promo successfully")
                .build();
    }

    @DeleteMapping("/{promdId}")
    public ApiResponse<Void> deletePromod(@PathVariable String promdId) {
        promoService.deletePromo(promdId);
        return ApiResponse.<Void>builder().message("Delete Promo successfully").build();
    }

    @PostMapping("/create_promo_manager")
    public ApiResponse<PromoReponse> createPromoMana(@RequestBody PromoRequest request) {
        return ApiResponse.<PromoReponse>builder()
                .data(promoService.createPromoManagement(request))
                .message("Create Promo successfully")
                .build();
    }

    @PutMapping("/update_promo_manager")
    public ApiResponse<PromoReponse> updatePromodManager(@RequestBody PromoUpdateRequest request) {
        return ApiResponse.<PromoReponse>builder()
                .data(promoService.updatePromoManagement(request))
                .message("update Promo successfully")
                .build();
    }

    @DeleteMapping("/delete_promo_manager/{promdId}")
    public ApiResponse<Void> deletePromodManager(@PathVariable String promdId) {
        promoService.deletePromoManagement(promdId);
        return ApiResponse.<Void>builder().message("Delete Promo successfully").build();
    }
}
