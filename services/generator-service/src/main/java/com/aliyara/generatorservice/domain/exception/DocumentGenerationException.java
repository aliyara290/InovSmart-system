package com.aliyara.generatorservice.domain.exception;

public class DocumentGenerationException extends DomainException {

    public DocumentGenerationException(String message) {
        super(message);
    }

    public DocumentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
