package com.aliyara.procurementservice.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Idempotency record: one row per delivered order.
 * Prevents duplicate stock-increase events being published for the same order.
 */
@Entity
@Table(name = "delivered_orders", uniqueConstraints = @UniqueConstraint(columnNames = "order_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveredOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "delivered_at", nullable = false)
    private LocalDateTime deliveredAt;
}
