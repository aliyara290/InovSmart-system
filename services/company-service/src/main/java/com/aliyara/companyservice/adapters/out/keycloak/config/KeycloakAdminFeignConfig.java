package com.aliyara.companyservice.adapters.out.keycloak.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class KeycloakAdminFeignConfig {

    @Bean
    public RequestInterceptor keycloakAdminRequestInterceptor(ApplicationContext context) {
        return template -> {
            KeycloakTokenProvider tokenProvider = context.getBean(KeycloakTokenProvider.class);
            String token = tokenProvider.getValidToken();
            template.header("Authorization", "Bearer " + token);
        };
    }
}
