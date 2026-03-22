package com.aliyara.billingservice.domain.exception;

public class StockReservationException extends DomainException {

    public StockReservationException(String message) {
        super(message);
    }

    public StockReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
