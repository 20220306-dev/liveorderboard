package com.example.orderboard.domain.delete;

import com.example.orderboard.domain.Order;
import com.example.orderboard.domain.OrderStore;
import com.example.orderboard.domain.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderDeleteService {

    private final OrderStore orderStore;

    public void delete(UserId userId, Order.OrderId orderId) {
        this.orderStore.findByUserIdAndOrderId(userId, orderId)
                .orElseThrow(OrderDoesNotExistException::new);
        orderStore.deleteByOrderId(orderId);
    }
}
