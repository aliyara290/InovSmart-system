package com.aliyara.billingservice.domain.exception;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
