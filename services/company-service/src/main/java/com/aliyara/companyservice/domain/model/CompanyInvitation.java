package com.aliyara.companyservice.domain.model;

import com.aliyara.companyservice.domain.enums.InvitationStatus;
import com.aliyara.companyservice.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class CompanyInvitation {
    private UUID id;
    private UUID tenantId;
    private UUID companyId;
    private String email;
    private UserRole role;
    private String token;
    private InvitationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public CompanyInvitation() {
    }

    public CompanyInvitation(UUID id, UUID tenantId, UUID companyId, String email, 
                            UserRole role, String token, InvitationStatus status, 
                            LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.email = email;
        this.role = role;
        this.token = token;
        this.status = status;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
