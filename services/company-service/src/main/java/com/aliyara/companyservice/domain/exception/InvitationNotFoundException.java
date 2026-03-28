package com.aliyara.companyservice.domain.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(String token) {
        super("Invitation not found for token: " + token);
    }
}
