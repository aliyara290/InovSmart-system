package com.aliyara.companyservice.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private final String id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean enabled;
}
