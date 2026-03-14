package com.aliyara.procurementservice.adapter.in.rest;

import com.aliyara.procurementservice.application.dto.order.CreateOrderRequest;
import com.aliyara.procurementservice.application.dto.order.OrderResponse;
import com.aliyara.procurementservice.application.port.in.OrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderUseCase.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable UUID id) {
        return orderUseCase.getOrder(id);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders(@RequestParam String tenantId) {
        return orderUseCase.getAllOrders(tenantId);
    }

    @PatchMapping("/{id}/send")
    public OrderResponse markAsSent(@PathVariable UUID id) {
        return orderUseCase.markAsSent(id);
    }

    @PatchMapping("/{id}/deliver")
    public OrderResponse markAsDelivered(@PathVariable UUID id) {
        return orderUseCase.markAsDelivered(id);
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID id) {
        return orderUseCase.cancelOrder(id);
    }
}
