package com.example.orderboard.domain;

import java.util.List;
import java.util.Optional;

public interface OrderStore {

    void insert(Order order);

    List<Order> findAll();

    Optional<Order> findByUserIdAndOrderId(UserId userId, Order.OrderId orderId);

    void deleteByOrderId(Order.OrderId orderId);
}
