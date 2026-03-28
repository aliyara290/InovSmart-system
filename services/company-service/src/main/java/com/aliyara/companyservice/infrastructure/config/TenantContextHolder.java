package com.aliyara.companyservice.infrastructure.config;

import java.util.UUID;

public class TenantContextHolder {
    private static final ThreadLocal<UUID> currentTenantId = new ThreadLocal<>();

    public static void setTenantId(UUID tenantId) {
        currentTenantId.set(tenantId);
    }

    public static UUID getTenantId() {
        return currentTenantId.get();
    }

    public static void clear() {
        currentTenantId.remove();
    }
}
