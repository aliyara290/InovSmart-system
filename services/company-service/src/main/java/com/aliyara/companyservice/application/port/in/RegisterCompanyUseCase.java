package com.aliyara.companyservice.application.port.in;

import com.aliyara.companyservice.interfaces.dto.request.RegisterCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.response.RegisterCompanyResponse;

public interface RegisterCompanyUseCase {
    RegisterCompanyResponse register(RegisterCompanyRequest request);
}
