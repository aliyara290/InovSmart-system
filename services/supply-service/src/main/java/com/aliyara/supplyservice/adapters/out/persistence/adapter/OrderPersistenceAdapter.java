package com.aliyara.supplyservice.adapters.out.persistence.adapter;

import com.aliyara.supplyservice.adapters.out.persistence.entity.OrderEntity;
import com.aliyara.supplyservice.adapters.out.persistence.jpa.OrderJpaRepository;
import com.aliyara.supplyservice.adapters.out.persistence.mapper.OrderPersistenceMapper;
import com.aliyara.supplyservice.application.port.out.OrderRepositoryPort;
import com.aliyara.supplyservice.domain.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository repository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findAllByTenantId(String tenantId) {
        return repository.findAllByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllByTenantIdAndSupplierId(String tenantId, UUID supplierId) {
        return repository.findAllByTenantIdAndSupplierId(tenantId, supplierId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public String generateOrderNumber(String tenantId) {
        int year = LocalDate.now().getYear();
        String prefix = "PO-" + year + "-";

        Optional<OrderEntity> lastOrder = repository.findTopByTenantIdOrderByOrderNumberDesc(tenantId);

        int nextSeq = 1;
        if (lastOrder.isPresent() && lastOrder.get().getOrderNumber() != null) {
            String lastNumber = lastOrder.get().getOrderNumber();
            try {
                String seqPart = lastNumber.substring(lastNumber.lastIndexOf('-') + 1);
                nextSeq = Integer.parseInt(seqPart) + 1;
            } catch (Exception ignored) {
            }
        }

        return prefix + String.format("%05d", nextSeq);
    }
}
