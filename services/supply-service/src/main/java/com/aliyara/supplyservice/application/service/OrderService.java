package com.aliyara.supplyservice.application.service;

import com.aliyara.supplyservice.application.dto.inventory.LowStockProductResponse;
import com.aliyara.supplyservice.application.dto.inventory.ProductInfo;
import com.aliyara.supplyservice.application.dto.inventory.StockInfo;
import com.aliyara.supplyservice.application.dto.order.*;
import com.aliyara.supplyservice.application.port.in.OrderUseCase;
import com.aliyara.supplyservice.application.port.out.InventoryPort;
import com.aliyara.supplyservice.application.port.out.OrderRepositoryPort;
import com.aliyara.supplyservice.application.port.out.SupplierRepositoryPort;
import com.aliyara.supplyservice.domain.exception.InsufficientStock;
import com.aliyara.supplyservice.domain.exception.OrderNotFoundException;
import com.aliyara.supplyservice.domain.exception.ProductNotFoundException;
import com.aliyara.supplyservice.domain.exception.SupplierNotFoundException;
import com.aliyara.supplyservice.domain.model.order.Order;
import com.aliyara.supplyservice.domain.model.order.OrderItem;
import com.aliyara.supplyservice.domain.model.order.enums.OrderStatus;
import com.aliyara.supplyservice.domain.model.supplier.Supplier;
import com.aliyara.supplyservice.infrastrecture.config.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final SupplierRepositoryPort supplierRepository;
    private final InventoryPort inventoryPort;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        String tenantId = TenantContextHolder.getTenantIdAsString();

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException(
                        "Supplier not found with id: " + request.getSupplierId()));

        List<OrderItem> items = new LinkedList<>();
        for (CreateOrderItemRequest itemReq : request.getItems()) {
            ProductInfo product = inventoryPort.getProduct(itemReq.getProductId());

            if(product == null) {
                throw new ProductNotFoundException("Product with id " + itemReq.getProductId() + " not found!");
            }

            items.add(new OrderItem.Builder()
                    .productId(itemReq.getProductId())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .build());
        }

        String orderNumber = orderRepository.generateOrderNumber(tenantId);
        String createdBy = getCurrentUserId();

        Order order = new Order.Builder()
                .tenantId(tenantId)
                .supplierId(request.getSupplierId())
                .orderNumber(orderNumber)
                .createdBy(createdBy)
                .notes(request.getNotes())
                .items(items)
                .build();

        Order saved = orderRepository.save(order);

        return toResponse(saved, supplier.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        String supplierName = supplierRepository.findById(order.getSupplierId())
                .map(Supplier::getName)
                .orElse("Unknown");

        return toResponse(order, supplierName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        String tenantId = TenantContextHolder.getTenantIdAsString();
        return orderRepository.findAllByTenantId(tenantId).stream()
                .map(order -> {
                    String supplierName = supplierRepository.findById(order.getSupplierId())
                            .map(Supplier::getName)
                            .orElse("Unknown");
                    return toResponse(order, supplierName);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersBySupplier(UUID supplierId) {
        String tenantId = TenantContextHolder.getTenantIdAsString();
        return orderRepository.findAllByTenantIdAndSupplierId(tenantId, supplierId).stream()
                .map(order -> {
                    String supplierName = supplierRepository.findById(order.getSupplierId())
                            .map(Supplier::getName)
                            .orElse("Unknown");
                    return toResponse(order, supplierName);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());

        switch (newStatus) {
            case SENT -> order.markAsSent();
            case DELIVERED -> {
                order.markAsDelivered();
                updateInventoryOnDelivery(order);
            }
            case CANCELLED -> order.cancel();
            default -> throw new IllegalArgumentException("Cannot manually set status to: " + newStatus);
        }

        Order saved = orderRepository.save(order);

        String supplierName = supplierRepository.findById(saved.getSupplierId())
                .map(Supplier::getName)
                .orElse("Unknown");

        return toResponse(saved, supplierName);
    }

    @Override
    @Transactional
    public void cancelOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        order.cancel();
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockProductResponse> getLowStockProducts() {
        List<StockInfo> allStocks = inventoryPort.getAllStocks();
        log.info("low stocks: {}", allStocks);
        return allStocks.stream()
                .filter(stock -> "LOW_STOCK".equals(stock.getStatus()) || "OUT_OF_STOCK".equals(stock.getStatus()))
                .map(stock -> {
                    String productName = "Unknown";
                    try {
                        ProductInfo product = inventoryPort.getProduct(stock.getProductId());
                        productName = product.getName();
                    } catch (Exception e) {
                        log.warn("Could not fetch product info for {}", stock.getProductId());
                    }

                    return LowStockProductResponse.builder()
                            .productId(stock.getProductId())
                            .productName(productName)
                            .currentStock(stock.getQuantityTotal())
                            .minStock(stock.getMinStock())
                            .stockStatus(stock.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void updateInventoryOnDelivery(Order order) {
        UUID performedBy = getCurrentUserUUID();
        for (OrderItem item : order.getItems()) {
            try {
                StockInfo currentStock = inventoryPort.getStockByProductId(item.getProductId());
                if (currentStock != null) {
                    int newTotal = currentStock.getQuantityTotal() + item.getQuantity();
                    inventoryPort.adjustStock(item.getProductId(), newTotal, order.getId(), performedBy);
                    log.info("Updated stock for product {} : {} -> {}",
                            item.getProductId(), currentStock.getQuantityTotal(), newTotal);
                }
            } catch (Exception e) {
                log.error("Failed to update stock for product {} on delivery: {}",
                        item.getProductId(), e.getMessage());
            }
        }
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        return null;
    }

    private UUID getCurrentUserUUID() {
        String userId = getCurrentUserId();
        if (userId != null) {
            try {
                return UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                log.warn("Could not parse JWT sub as UUID: {}", userId);
            }
        }
        return null;
    }

    private OrderResponse toResponse(Order order, String supplierName) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.subtotal())
                        .deliveredQuantity(item.getDeliveredQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .supplierId(order.getSupplierId())
                .supplierName(supplierName)
                .status(order.getStatus().name())
                .notes(order.getNotes())
                .totalExcludingTax(order.getTotalExcludingTax())
                .totalTTC(order.getTotalTTC())
                .createdBy(order.getCreatedBy())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
