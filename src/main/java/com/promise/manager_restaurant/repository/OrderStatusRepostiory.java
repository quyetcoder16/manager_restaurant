package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepostiory extends JpaRepository<OrderStatus, String> {
}
