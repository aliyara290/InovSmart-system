package com.aliyara.supplyservice.adapters.in.rest;

import com.aliyara.supplyservice.application.dto.inventory.LowStockProductResponse;
import com.aliyara.supplyservice.application.dto.order.CreateOrderRequest;
import com.aliyara.supplyservice.application.dto.order.OrderResponse;
import com.aliyara.supplyservice.application.dto.order.UpdateOrderStatusRequest;
import com.aliyara.supplyservice.application.port.in.OrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderUseCase.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderUseCase.getOrder(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderUseCase.getAllOrders());
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<List<OrderResponse>> getOrdersBySupplier(@PathVariable UUID supplierId) {
        return ResponseEntity.ok(orderUseCase.getOrdersBySupplier(supplierId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable UUID id,
                                                            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderUseCase.updateOrderStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderUseCase.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock-products")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<LowStockProductResponse>> getLowStockProducts() {
        return ResponseEntity.ok(orderUseCase.getLowStockProducts());
    }
}
