package com.order_service.order_service.controller;

import com.order_service.order_service.DTO.OrderRequestDTO;
import com.order_service.order_service.DTO.OrderResponseDTO;
import com.order_service.order_service.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO order) {
        OrderResponseDTO responseDTO = orderService.createOrder(order);
        return ResponseEntity.ok(responseDTO);
    }
}
