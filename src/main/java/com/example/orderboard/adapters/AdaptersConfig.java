package com.example.orderboard.adapters;

import org.springframework.context.annotation.Import;

@Import({InMemoryOrderStore.class,
        SimpleOrderIdGenerator.class})
public class AdaptersConfig {
}
