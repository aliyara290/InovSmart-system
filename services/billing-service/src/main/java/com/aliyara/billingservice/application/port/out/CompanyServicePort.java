package com.aliyara.billingservice.application.port.out;

import com.aliyara.billingservice.domain.model.common.CompanySnapshot;

public interface CompanyServicePort {

    CompanySnapshot getCompanySnapshot(String tenantId);
}
