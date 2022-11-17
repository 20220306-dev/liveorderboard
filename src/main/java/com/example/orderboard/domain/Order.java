package com.example.orderboard.domain;

import lombok.Builder;

import java.util.UUID;

public record Order(OrderId id,
                    UserId userId,
                    OrderType type,
                    PricePerUnit pricePerUnit,
                    Quantity quantity) {

    @Builder
    public Order {
    }

    public record OrderId(UUID id) {
    }
}
