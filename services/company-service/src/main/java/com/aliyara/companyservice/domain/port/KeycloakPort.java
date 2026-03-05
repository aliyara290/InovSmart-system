package com.aliyara.companyservice.domain.port;

import com.aliyara.companyservice.domain.model.User;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import java.util.List;

public interface KeycloakPort {
    void createTenantGroup(TenantId tenantId);

    List<User> getUsersByTenantId(TenantId tenantId);
}
