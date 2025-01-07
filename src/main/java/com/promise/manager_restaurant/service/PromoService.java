package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.promo.PromoRequest;
import com.promise.manager_restaurant.dto.request.promo.PromoUpdateRequest;
import com.promise.manager_restaurant.dto.response.promo.PromoReponse;
import com.promise.manager_restaurant.entity.Promo;
import org.springframework.stereotype.Service;

@Service
public interface PromoService {
    public PromoReponse createPromo(PromoRequest promo);

    public PromoReponse updatePromo(PromoUpdateRequest promo);

    public void deletePromo(String requestId);

    public PromoReponse createPromoManagement(PromoRequest promo);

    public PromoReponse updatePromoManagement(PromoUpdateRequest promo);

    public void deletePromoManagement(String requestId);
}
