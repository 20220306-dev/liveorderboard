package com.example.orderboard.interfaces;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.example.orderboard.domain.Quantity.Unit;

record OrderRegistrationRequestForm(PricePerUnit pricePerUnit, Quantity quantity) {

    record PricePerUnit(BigDecimal amount, String currencyCode) {
    }

    record Quantity(BigInteger amount, Unit unit) {
    }
}
