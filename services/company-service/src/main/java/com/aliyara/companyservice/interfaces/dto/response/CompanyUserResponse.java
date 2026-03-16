package com.aliyara.companyservice.interfaces.dto.response;

import com.aliyara.companyservice.domain.enums.UserStatus;
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
public class CompanyUserResponse {
    private UUID id;
    private UUID tenantId;
    private UUID companyId;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private UUID invitedBy;
    private LocalDateTime joinedAt;
    private LocalDateTime createdAt;
}
