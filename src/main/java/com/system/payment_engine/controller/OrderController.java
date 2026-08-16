package com.system.payment_engine.controller;

import com.system.payment_engine.dto.CreateOrderDTO;
import com.system.payment_engine.model.Order;
import com.system.payment_engine.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderDTO dto) {
        Order order = orderService.processOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
