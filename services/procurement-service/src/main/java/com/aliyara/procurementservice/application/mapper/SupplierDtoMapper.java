package com.aliyara.procurementservice.application.mapper;

import com.aliyara.procurementservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.procurementservice.application.dto.supplier.SupplierResponse;
import com.aliyara.procurementservice.domain.models.supplier.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierDtoMapper {

    default Supplier toDomain(CreateSupplierRequest request) {
        if (request == null)
            return null;
        return new Supplier.Builder()
                .tenantId(request.getTenantId())
                .name(request.getName())
                .contactEmail(request.getContactEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    SupplierResponse toResponse(Supplier supplier);
}
