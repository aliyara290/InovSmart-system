package com.aliyara.companyservice.domain.model;

import com.aliyara.companyservice.domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class CompanyUser {
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

    public CompanyUser() {
    }

    public CompanyUser(UUID id, UUID tenantId, UUID companyId, UUID userId, 
                       String email, String firstName, String lastName, 
                       UserStatus status, UUID invitedBy, LocalDateTime joinedAt, 
                       LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.invitedBy = invitedBy;
        this.joinedAt = joinedAt;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UUID getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(UUID invitedBy) {
        this.invitedBy = invitedBy;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
