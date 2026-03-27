package com.aliyara.supplyservice.infrastrecture.config;

import java.util.UUID;

public class TenantContextHolder {

    private static final ThreadLocal<UUID> currentTenantId = new ThreadLocal<>();

    public static UUID getTenantId() {
        return currentTenantId.get();
    }

    public static void setTenantId(UUID tenantId) {
        currentTenantId.set(tenantId);
    }

    public static String getTenantIdAsString() {
        UUID tenantId = currentTenantId.get();
        return tenantId != null ? tenantId.toString() : null;
    }

    public static void clear() {
        currentTenantId.remove();
    }
}
