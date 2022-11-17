package com.example.orderboard.domain;

import java.math.BigInteger;

public record Quantity(BigInteger amount, Unit unit) {

    public Quantity {
        // TODO validate input
    }

    static Quantity of(BigInteger amount, Unit unit) {
        // TODO validate input
        return new Quantity(amount, unit);
    }

    public enum Unit {
        KG
    }
}
