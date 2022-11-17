package com.example.orderboard.adapters;

import com.example.orderboard.domain.Order;
import com.example.orderboard.domain.OrderStore;
import com.example.orderboard.domain.UserId;
import com.example.orderboard.domain.register.OrderIdAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryOrderStore implements OrderStore {

    private final Map<Order.OrderId, Order> store = new ConcurrentHashMap<>();

    @Override
    public void insert(Order order) {
        boolean isAlreadyStored = store.putIfAbsent(order.id(), order) != null;
        if (isAlreadyStored) {
            throw new OrderIdAlreadyExistsException();
        }
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Order> findByUserIdAndOrderId(UserId userId, Order.OrderId orderId) {
        return store.values().stream()
                .filter(order -> order.userId().equals(userId))
                .filter(order -> order.id().equals(orderId))
                .findFirst();
    }

    @Override
    public void deleteByOrderId(Order.OrderId orderId) {
        this.store.remove(orderId);
    }
}
