package com.aliyara.procurementservice.adapter.out.persistence;

import com.aliyara.procurementservice.adapter.out.persistence.jpa.OrderJpaRepository;
import com.aliyara.procurementservice.adapter.out.persistence.mapper.OrderPersistenceMapper;
import com.aliyara.procurementservice.domain.models.order.Order;
import com.aliyara.procurementservice.domain.port.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Order save(Order order) {
        var entity = orderPersistenceMapper.toEntity(order);
        var saved = orderJpaRepository.save(entity);
        return orderPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id)
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findAllByTenantId(String tenantId) {
        return orderJpaRepository.findAllByTenantId(tenantId)
                .stream()
                .map(orderPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        orderJpaRepository.deleteById(id);
    }
}
