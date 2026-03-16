package com.aliyara.companyservice.interfaces.dto.response;

import com.aliyara.companyservice.domain.enums.InvitationStatus;
import com.aliyara.companyservice.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInvitationResponse {
    private UUID id;
    private UUID tenantId;
    private UUID companyId;
    private String email;
    private UserRole role;
    private InvitationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
