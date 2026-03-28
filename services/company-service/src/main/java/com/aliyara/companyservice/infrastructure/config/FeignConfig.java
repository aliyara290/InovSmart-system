package com.aliyara.companyservice.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.aliyara.companyservice.adapters.out.keycloak.client")
public class FeignConfig {
}
