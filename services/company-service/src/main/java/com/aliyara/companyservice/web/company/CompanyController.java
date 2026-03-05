package com.aliyara.companyservice.web.company;

import com.aliyara.companyservice.application.dto.CompanyRequest;
import com.aliyara.companyservice.application.dto.CompanyResponse;
import com.aliyara.companyservice.application.dto.CompanyUserResponse;
import com.aliyara.companyservice.application.service.CompanyUseCaseService;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyUseCaseService companyUseCaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse registerCompany(@Valid @RequestBody CompanyRequest request) {
        return companyUseCaseService.registerCompany(request);
    }

    @GetMapping("/{companyId}/users")
    public List<CompanyUserResponse> getCompanyUsers(@PathVariable("companyId") UUID companyId) {
        return companyUseCaseService.getCompanyUsers(new CompanyId(companyId));
    }
}
