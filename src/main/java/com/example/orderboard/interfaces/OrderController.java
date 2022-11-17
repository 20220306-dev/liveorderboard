package com.example.orderboard.interfaces;

import com.example.orderboard.domain.*;
import com.example.orderboard.domain.delete.OrderDeleteService;
import com.example.orderboard.domain.register.OrderRegistrationForm;
import com.example.orderboard.domain.register.OrderRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.orderboard.interfaces.OrderRegistrationResponse.OrderId;

@RestController
@RequiredArgsConstructor
class OrderController {

    private static final String USER_ID_HEADER_KEY = "X-AUTH-USER-ID";

    private final OrderRegistrationService registrationService;
    private final OrderDeleteService deleteService;
    private final OrderStore orderStore;

    @GetMapping(value = "/orders/boardView", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderBoardView> getBoardView() {
        List<Order> orders = orderStore.findAll();
        return ResponseEntity.ok(new OrderBoardView(orders));
    }

    @PostMapping("/orders/buy")
    public OrderRegistrationResponse createBuyOrder(@RequestBody @Validated OrderRegistrationRequestForm request,
                                                    @RequestHeader(USER_ID_HEADER_KEY) String userId) {
        var orderId = registrationService.registerBuy(new UserId(userId), toRegistrationForm(request));
        return new OrderRegistrationResponse(new OrderId(orderId.id()));
    }

    @PostMapping("/orders/sell")
    public OrderRegistrationResponse createSellOrder(@RequestBody @Validated OrderRegistrationRequestForm request,
                                                     @RequestHeader(USER_ID_HEADER_KEY) String userId) {
        var orderId = registrationService.registerSell(new UserId(userId), toRegistrationForm(request));
        return new OrderRegistrationResponse(new OrderId(orderId.id()));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteByOrderId(@PathVariable("orderId") UUID orderId,
                                                @RequestHeader(USER_ID_HEADER_KEY) String userId) {
        deleteService.delete(new UserId(userId), new Order.OrderId(orderId));
        return ResponseEntity.status(204).build();
    }


    private OrderRegistrationForm toRegistrationForm(OrderRegistrationRequestForm request) {
        return OrderRegistrationForm.builder()
                .quantity(new Quantity(request.quantity().amount(), request.quantity().unit()))
                .pricePerUnit(
                        new PricePerUnit(request.pricePerUnit().amount(),
                                new PricePerUnit.CurrencyCode(request.pricePerUnit().currencyCode())))
                .build();
    }
}
