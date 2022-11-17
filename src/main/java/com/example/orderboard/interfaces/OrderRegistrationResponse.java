package com.example.orderboard.interfaces;

import java.util.UUID;

record OrderRegistrationResponse(OrderId uuid) {

    record OrderId(UUID id) { }
}
