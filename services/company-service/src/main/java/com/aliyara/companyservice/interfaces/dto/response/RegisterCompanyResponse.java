package com.aliyara.companyservice.interfaces.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyResponse {
    private UUID tenantId;
    private UUID companyId;
    private UUID userId;
    private String accessToken;
    private String refreshToken;
}
