package com.aliyara.companyservice.domain.exception;

public class InvitationAlreadyAcceptedException extends RuntimeException {
    public InvitationAlreadyAcceptedException(String token) {
        super("Invitation with token '" + token + "' has already been accepted");
    }
}
