package com.aliyara.companyservice.domain.exception;

public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException(String token) {
        super("Invitation with token '" + token + "' has expired");
    }
}
