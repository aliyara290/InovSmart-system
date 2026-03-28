package com.aliyara.inventoryservice.domain.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(UUID categoryId) {
        super("Category not found with id: " + categoryId);
    }

    public CategoryNotFoundException(String name) {
        super("Category not found with name: " + name);
    }

    public CategoryNotFoundException(UUID categoryId, String tenantId) {
        super("Category not found with id: " + categoryId + " for tenant: " + tenantId);
    }
}
