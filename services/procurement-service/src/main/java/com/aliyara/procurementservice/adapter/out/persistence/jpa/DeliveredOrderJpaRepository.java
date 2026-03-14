package com.aliyara.procurementservice.adapter.out.persistence.jpa;

import com.aliyara.procurementservice.adapter.out.persistence.entity.DeliveredOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveredOrderJpaRepository extends JpaRepository<DeliveredOrderEntity, Long> {
    boolean existsByOrderId(UUID orderId);
}
