package com.example.orderboard.domain;

import com.example.orderboard.domain.delete.OrderDeleteService;
import com.example.orderboard.domain.register.OrderRegistrationService;
import org.springframework.context.annotation.Import;

@Import({OrderRegistrationService.class, OrderDeleteService.class})
public class DomainConfig {
}
