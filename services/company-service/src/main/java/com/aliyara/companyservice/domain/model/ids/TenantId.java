package com.aliyara.companyservice.domain.model.ids;

import java.util.UUID;

public record TenantId(UUID id) {
    public TenantId {
        if (id == null) {
            throw new IllegalArgumentException("TenantId cannot be null");
        }
    }

    public TenantId() {
        this(UUID.randomUUID());
    }
}
