package com.aliyara.supplyservice.adapters.out.persistence.mapper;

import com.aliyara.supplyservice.adapters.out.persistence.entity.SupplierEntity;
import com.aliyara.supplyservice.domain.model.supplier.Supplier;
import com.aliyara.supplyservice.domain.model.supplier.enums.SupplierStatus;
import org.springframework.stereotype.Component;

@Component
public class SupplierPersistenceMapper {

    public Supplier toDomain(SupplierEntity entity) {
        return new Supplier.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .contactEmail(entity.getContactEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .status(SupplierStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public SupplierEntity toEntity(Supplier domain) {
        return SupplierEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .contactEmail(domain.getContactEmail())
                .phone(domain.getPhone())
                .address(domain.getAddress())
                .status(domain.getStatus().name())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
