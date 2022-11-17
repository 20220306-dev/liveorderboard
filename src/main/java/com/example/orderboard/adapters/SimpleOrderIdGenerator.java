package com.example.orderboard.adapters;

import com.example.orderboard.domain.Order;
import com.example.orderboard.domain.register.OrderIdGenerator;

import java.util.UUID;

class SimpleOrderIdGenerator implements OrderIdGenerator {
    @Override
    public Order.OrderId generate() {
        return new Order.OrderId(UUID.randomUUID());
    }
}
