package com.aliyara.companyservice.application.port.in;

import com.aliyara.companyservice.interfaces.dto.request.AcceptInvitationRequest;
import com.aliyara.companyservice.interfaces.dto.request.InviteUserRequest;
import com.aliyara.companyservice.interfaces.dto.response.RegisterCompanyResponse;

import java.util.UUID;

public interface InviteUserUseCase {
    void invite(UUID tenantId, InviteUserRequest request);
    RegisterCompanyResponse acceptInvitation(AcceptInvitationRequest request);
}
