package com.aliyara.billingservice.infrastrecture.config;

import java.util.UUID;

public class TenantContextHolder {
    private static final ThreadLocal<String> currentTenantId = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        currentTenantId.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenantId.get();
    }

    public static void clear() {
        currentTenantId.remove();
    }
}
