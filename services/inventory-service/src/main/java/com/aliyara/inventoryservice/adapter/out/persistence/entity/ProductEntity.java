package com.aliyara.inventoryservice.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = { "sku", "tenant_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String sku;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "category_id")
    private UUID categoryId;
}
