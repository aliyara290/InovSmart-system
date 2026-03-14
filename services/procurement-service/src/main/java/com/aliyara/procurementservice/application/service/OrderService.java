package com.aliyara.procurementservice.application.service;

import com.aliyara.procurementservice.application.dto.order.CreateOrderRequest;
import com.aliyara.procurementservice.application.dto.order.OrderResponse;
import com.aliyara.procurementservice.application.event.StockIncreaseEvent;
import com.aliyara.procurementservice.application.mapper.OrderDtoMapper;
import com.aliyara.procurementservice.application.port.in.OrderUseCase;
import com.aliyara.procurementservice.application.port.out.InventoryClientPort;
import com.aliyara.procurementservice.application.port.out.OrderEventPublisherPort;
import com.aliyara.procurementservice.domain.exception.DuplicateDeliveryException;
import com.aliyara.procurementservice.domain.exception.OrderNotFoundException;
import com.aliyara.procurementservice.domain.exception.ProductNotFoundException;
import com.aliyara.procurementservice.domain.exception.SupplierNotFoundException;
import com.aliyara.procurementservice.domain.models.order.Order;
import com.aliyara.procurementservice.domain.models.order.OrderItem;
import com.aliyara.procurementservice.domain.port.DeliveredOrderRepository;
import com.aliyara.procurementservice.domain.port.OrderRepository;
import com.aliyara.procurementservice.domain.port.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final DeliveredOrderRepository deliveredOrderRepository;
    private final InventoryClientPort inventoryClientPort;
    private final OrderEventPublisherPort eventPublisherPort;
    private final OrderDtoMapper orderDtoMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for tenantId={}, supplierId={}", request.getTenantId(), request.getSupplierId());

        supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException(
                        "Supplier not found with id: " + request.getSupplierId()));

        request.getItems().forEach(item -> {
            UUID productId = item.getProductId();
            if (!inventoryClientPort.productExists(productId)) {
                throw new ProductNotFoundException(
                        "Product not found in Inventory Service with id: " + productId);
            }
        });

        List<OrderItem> domainItems = orderDtoMapper.toOrderItemList(request.getItems());
        Order order = new Order.Builder()
                .tenantId(request.getTenantId())
                .supplierId(request.getSupplierId())
                .notes(request.getNotes())
                .items(domainItems)
                .build();

        Order saved = orderRepository.save(order);
        log.info("Order created with id={}", saved.getId());
        return orderDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return orderDtoMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(String tenantId) {
        return orderRepository.findAllByTenantId(tenantId)
                .stream()
                .map(orderDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse markAsSent(UUID id) {
        log.info("Marking order id={} as SENT", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        order.markAsSent();
        return orderDtoMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse markAsDelivered(UUID id) {
        log.info("Marking order id={} as DELIVERED", id);

        // 1. Idempotency check
        if (deliveredOrderRepository.existsByOrderId(id)) {
            throw new DuplicateDeliveryException(
                    "Order with id=" + id + " has already been processed for delivery.");
        }

        // 2. Transition status
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        order.markAsDelivered();
        Order saved = orderRepository.save(order);

        // 3. Record idempotency key
        deliveredOrderRepository.save(id);

        // 4. Publish one StockIncreaseEvent per order item
        saved.getItems().forEach(item -> {
            StockIncreaseEvent event = new StockIncreaseEvent(saved.getId(), item.getProductId(), item.getQuantity());
            eventPublisherPort.publishStockIncreaseEvent(event);
            log.info("Published StockIncreaseEvent: orderId={}, productId={}, qty={}",
                    event.orderId(), event.productId(), event.quantity());
        });

        return orderDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID id) {
        log.info("Cancelling order id={}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        order.cancel();
        return orderDtoMapper.toResponse(orderRepository.save(order));
    }
}
