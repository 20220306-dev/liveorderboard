package com.example.orderboard.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PricePerUnit(BigDecimal amount, CurrencyCode currencyCode) {

    public PricePerUnit {
        // TODO validate input
    }

    static PricePerUnit of(BigDecimal amount, CurrencyCode currency) {
        // TODO validate input
        return new PricePerUnit(amount, currency);
    }

    public record CurrencyCode(String code){}
}
