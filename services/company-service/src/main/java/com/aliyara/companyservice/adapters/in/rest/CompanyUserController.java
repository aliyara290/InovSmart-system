package com.aliyara.companyservice.adapters.in.rest;

import com.aliyara.companyservice.application.port.in.ManageCompanyUsersUseCase;
import com.aliyara.companyservice.interfaces.dto.request.ChangeUserRoleRequest;
import com.aliyara.companyservice.interfaces.dto.request.UpdateStatusRequest;
import com.aliyara.companyservice.interfaces.dto.response.CompanyUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/{tenantId}/users")
public class CompanyUserController {

    private final ManageCompanyUsersUseCase manageCompanyUsersUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<List<CompanyUserResponse>> getUsers(@PathVariable UUID tenantId) {
        List<CompanyUserResponse> users = manageCompanyUsersUseCase.getUsersByTenantId(tenantId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<CompanyUserResponse> getUser(@PathVariable UUID tenantId,
                                                       @PathVariable UUID userId) {
        CompanyUserResponse user = manageCompanyUsersUseCase.getUserById(tenantId, userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> changeUserRole(@PathVariable UUID tenantId,
                                               @PathVariable UUID userId,
                                               @Valid @RequestBody ChangeUserRoleRequest request) {
        manageCompanyUsersUseCase.changeUserRole(tenantId, userId, request.getRole());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> changeUserStatus(@PathVariable UUID tenantId,
                                                 @PathVariable UUID userId,
                                                 @Valid @RequestBody UpdateStatusRequest request) {
        manageCompanyUsersUseCase.changeUserStatus(tenantId, userId, request.getUserStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> removeUser(@PathVariable UUID tenantId,
                                           @PathVariable UUID userId) {
        manageCompanyUsersUseCase.removeUser(tenantId, userId);
        return ResponseEntity.noContent().build();
    }
}
