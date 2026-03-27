package com.aliyara.billingservice.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanySnapshot {

    private final String name;
    private final String email;
    private final String phone;
    private final String address;
}
