package com.aliyara.companyservice.infrastructure.security;

import com.aliyara.companyservice.domain.exception.UnauthorizedTenantAccessException;
import com.aliyara.companyservice.infrastructure.config.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TenantSecurityService {

    public void validateTenantAccess(UUID requestedTenantId) {
        UUID userTenantId = TenantContextHolder.getTenantId();
        
        if (userTenantId == null || !userTenantId.equals(requestedTenantId)) {
            throw new UnauthorizedTenantAccessException(requestedTenantId, userTenantId);
        }
    }
}