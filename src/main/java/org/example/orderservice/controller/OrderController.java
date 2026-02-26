package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.CreateOrderRequest;
import org.example.orderservice.entity.Order;
import org.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private  final OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(@Valid @RequestBody CreateOrderRequest request,@RequestHeader("Idempotence") String idempotence) {
        System.out.println(request.toString() + idempotence);
        return orderService.create(request, idempotence);
    }



}
