package com.aliyara.companyservice.application.dto;

import lombok.Data;

@Data
public class CompanyUserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
}
