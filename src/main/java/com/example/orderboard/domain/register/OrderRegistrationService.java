package com.example.orderboard.domain.register;

import com.example.orderboard.domain.Order;
import com.example.orderboard.domain.OrderStore;
import com.example.orderboard.domain.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRegistrationService {

    private final OrderStore orderStore;
    private final OrderIdGenerator orderIdGenerator;

    public Order.OrderId registerBuy(UserId userId, OrderRegistrationForm orderForm) {
        Order.OrderId orderId = orderIdGenerator.generate();
        orderStore.insert(orderForm.toOrderBuy(userId, orderId));
        return orderId;
    }

    public Order.OrderId registerSell(UserId userId, OrderRegistrationForm orderForm) {
        Order.OrderId orderId = orderIdGenerator.generate();
        orderStore.insert(orderForm.toOrderSell(userId, orderId));
        return orderId;
    }
}
