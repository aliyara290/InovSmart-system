package com.aliyara.companyservice.domain.exception;

public class DuplicateCompanyNameException extends RuntimeException {

    public DuplicateCompanyNameException(String name) {
        super("A company with the name '" + name + "' is already registered");
    }
}
