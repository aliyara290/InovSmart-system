package com.aliyara.companyservice.domain.exception;

public class DuplicateTenantException extends RuntimeException {
    public DuplicateTenantException(String message) {
        super(message);
    }
}
