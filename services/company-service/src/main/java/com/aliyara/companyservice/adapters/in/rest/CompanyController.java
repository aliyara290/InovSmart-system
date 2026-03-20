package com.aliyara.companyservice.adapters.in.rest;

import com.aliyara.companyservice.application.port.in.ManageCompanyUseCase;
import com.aliyara.companyservice.application.port.in.RegisterCompanyUseCase;
import com.aliyara.companyservice.interfaces.dto.request.RegisterCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.request.UpdateCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.request.UpdateStatusRequest;
import com.aliyara.companyservice.interfaces.dto.response.CompanyResponse;
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
public class CompanyController {

    private final RegisterCompanyUseCase registerCompanyUseCase;
    private final ManageCompanyUseCase manageCompanyUseCase;

    @PostMapping("/register")
    public ResponseEntity<RegisterCompanyResponse> register(@Valid @RequestBody RegisterCompanyRequest request) {
        RegisterCompanyResponse response = registerCompanyUseCase.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> register() {
        String response = "Hello world!";
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{tenantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable UUID tenantId) {
        CompanyResponse response = manageCompanyUseCase.getByTenantId(tenantId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{tenantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable UUID tenantId,
                                                         @Valid @RequestBody UpdateCompanyRequest request) {
        CompanyResponse response = manageCompanyUseCase.update(tenantId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{tenantId}/status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> updateCompanyStatus(@PathVariable UUID tenantId,
                                                    @Valid @RequestBody UpdateStatusRequest request) {
        manageCompanyUseCase.updateStatus(tenantId, request.getCompanyStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tenantId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID tenantId) {
        manageCompanyUseCase.delete(tenantId);
        return ResponseEntity.noContent().build();
    }
}
