package com.aliyara.companyservice.interfaces.dto.request;

import com.aliyara.companyservice.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleRequest {

    @NotNull(message = "Role is required")
    private UserRole role;
}
