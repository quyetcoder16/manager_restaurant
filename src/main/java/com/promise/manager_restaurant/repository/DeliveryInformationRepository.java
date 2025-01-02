package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.DeliveryInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryInformationRepository extends JpaRepository<DeliveryInformation, String> {
}
