package com.example.orderboard.domain.register;

import com.example.orderboard.domain.Order;

public interface OrderIdGenerator {

    Order.OrderId generate();
}
