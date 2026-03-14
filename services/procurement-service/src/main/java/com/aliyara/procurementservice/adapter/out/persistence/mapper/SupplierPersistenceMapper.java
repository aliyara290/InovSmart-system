package com.aliyara.procurementservice.adapter.out.persistence.mapper;

import com.aliyara.procurementservice.adapter.out.persistence.entity.SupplierEntity;
import com.aliyara.procurementservice.domain.models.supplier.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierPersistenceMapper {

    default Supplier toDomain(SupplierEntity entity) {
        if (entity == null)
            return null;
        return new Supplier.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .contactEmail(entity.getContactEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    default SupplierEntity toEntity(Supplier supplier) {
        if (supplier == null)
            return null;
        SupplierEntity entity = new SupplierEntity();
        entity.setId(supplier.getId());
        entity.setTenantId(supplier.getTenantId());
        entity.setName(supplier.getName());
        entity.setContactEmail(supplier.getContactEmail());
        entity.setPhone(supplier.getPhone());
        entity.setAddress(supplier.getAddress());
        entity.setStatus(supplier.getStatus());
        entity.setCreatedAt(supplier.getCreatedAt());
        entity.setUpdatedAt(supplier.getUpdatedAt());
        return entity;
    }
}
