package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.OrderDetail;
import com.promise.manager_restaurant.entity.Orders;
import com.promise.manager_restaurant.entity.keys.KeyOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderDetail, KeyOrderDetail> {
    int deleteAllByOrder(Orders order);
}
