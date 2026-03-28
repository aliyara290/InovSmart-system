package com.aliyara.billingservice.domain.exception;

public class DuplicateInvoiceException extends DomainException {

    public DuplicateInvoiceException(String message) {
        super(message);
    }
}
