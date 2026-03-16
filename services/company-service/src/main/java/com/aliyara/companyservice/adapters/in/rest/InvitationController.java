package com.aliyara.companyservice.adapters.in.rest;

import com.aliyara.companyservice.application.port.in.InviteUserUseCase;
import com.aliyara.companyservice.interfaces.dto.request.AcceptInvitationRequest;
import com.aliyara.companyservice.interfaces.dto.request.InviteUserRequest;
import com.aliyara.companyservice.interfaces.dto.response.RegisterCompanyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class InvitationController {

    private final InviteUserUseCase inviteUserUseCase;

    @PostMapping("/{tenantId}/users/invite")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> inviteUser(@PathVariable UUID tenantId,
                                           @Valid @RequestBody InviteUserRequest request) {
        inviteUserUseCase.invite(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invitations/accept")
    public ResponseEntity<RegisterCompanyResponse> acceptInvitation(@Valid @RequestBody AcceptInvitationRequest request) {
        RegisterCompanyResponse response = inviteUserUseCase.acceptInvitation(request);
        return ResponseEntity.ok(response);
    }
}
