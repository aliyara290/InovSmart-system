package com.aliyara.companyservice.domain.exception;

import java.util.UUID;

public class UnauthorizedTenantAccessException extends RuntimeException {
    public UnauthorizedTenantAccessException(UUID requestedTenantId, UUID userTenantId) {
        super("Unauthorized access to tenantId: " + requestedTenantId + ". User belongs to tenantId: " + userTenantId);
    }
}
