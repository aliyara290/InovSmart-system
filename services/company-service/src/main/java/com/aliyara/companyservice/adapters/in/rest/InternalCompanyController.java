package com.aliyara.companyservice.adapters.in.rest;

import com.aliyara.companyservice.application.port.in.ManageCompanyUseCase;
import com.aliyara.companyservice.interfaces.dto.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/companies")
public class InternalCompanyController {

    private final ManageCompanyUseCase manageCompanyUseCase;

    @GetMapping("/{tenantId}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable UUID tenantId) {
        CompanyResponse response = manageCompanyUseCase.getByTenantId(tenantId);
        return ResponseEntity.ok(response);
    }
}
