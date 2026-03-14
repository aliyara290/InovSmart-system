package com.aliyara.procurementservice.adapter.out.persistence;

import com.aliyara.procurementservice.adapter.out.persistence.entity.DeliveredOrderEntity;
import com.aliyara.procurementservice.adapter.out.persistence.jpa.DeliveredOrderJpaRepository;
import com.aliyara.procurementservice.domain.port.DeliveredOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveredOrderRepositoryAdapter implements DeliveredOrderRepository {

    private final DeliveredOrderJpaRepository deliveredOrderJpaRepository;

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return deliveredOrderJpaRepository.existsByOrderId(orderId);
    }

    @Override
    public void save(UUID orderId) {
        DeliveredOrderEntity entity = new DeliveredOrderEntity();
        entity.setOrderId(orderId);
        entity.setDeliveredAt(LocalDateTime.now());
        deliveredOrderJpaRepository.save(entity);
    }
}
