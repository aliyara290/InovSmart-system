package com.aliyara.supplyservice.domain.exception;

public class DuplicateDeliveryException extends RuntimeException {
    public DuplicateDeliveryException(String message) {
        super(message);
    }
}
