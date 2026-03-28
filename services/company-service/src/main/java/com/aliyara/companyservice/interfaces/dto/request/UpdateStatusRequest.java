package com.aliyara.companyservice.interfaces.dto.request;

import com.aliyara.companyservice.domain.enums.CompanyStatus;
import com.aliyara.companyservice.domain.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private CompanyStatus companyStatus;

    private UserStatus userStatus;
}
