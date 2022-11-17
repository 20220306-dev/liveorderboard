package com.example.orderboard.domain.register;

import com.example.orderboard.domain.Order;
import com.example.orderboard.domain.PricePerUnit;
import com.example.orderboard.domain.Quantity;
import com.example.orderboard.domain.UserId;
import lombok.Builder;

import static com.example.orderboard.domain.OrderType.BUY;
import static com.example.orderboard.domain.OrderType.SELL;

public record OrderRegistrationForm(PricePerUnit pricePerUnit,
                                    Quantity quantity) {

    @Builder
    public OrderRegistrationForm {
    }

    Order toOrderBuy(UserId userId, Order.OrderId orderId) {
        return Order.builder()
                .id(orderId)
                .quantity(this.quantity())
                .pricePerUnit(this.pricePerUnit())
                .userId(userId)
                .type(BUY)
                .build();
    }

    Order toOrderSell(UserId userId, Order.OrderId orderId) {
        return Order.builder()
                .id(orderId)
                .quantity(this.quantity())
                .pricePerUnit(this.pricePerUnit())
                .userId(userId)
                .type(SELL)
                .build();
    }
}
