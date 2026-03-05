package com.aliyara.companyservice.domain.model.ids;

import java.util.UUID;

public record CompanyId(UUID id) {
    public CompanyId {
        if (id == null) {
            throw new IllegalArgumentException("AddressId cannot be null");
        }
    }

    public CompanyId() {
        this(UUID.randomUUID());
    }
}