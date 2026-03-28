package com.aliyara.billingservice.domain.exception;

public class InvoiceNotFoundException extends DomainException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
