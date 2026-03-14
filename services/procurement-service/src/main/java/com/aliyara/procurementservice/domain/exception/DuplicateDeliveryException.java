package com.aliyara.procurementservice.domain.exception;

public class DuplicateDeliveryException extends RuntimeException {
    public DuplicateDeliveryException(String message) {
        super(message);
    }
}
