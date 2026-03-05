package com.aliyara.inventoryservice.domain.exception;

public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name, String tenantId) {
        super(String.format(
                "Category with name '%s' already exists for tenant '%s'",
                name, tenantId));
    }
}
