package com.aliyara.procurementservice.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
@Configuration
public class FeignConfig {

    /**
     * Forwards the JWT Bearer token from the current security context
     * to outgoing Feign calls (service-to-service communication).
     */
    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtToken) {
                String tokenValue = jwtToken.getToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
                log.debug("Forwarding JWT token to downstream service: {}", requestTemplate.url());
            } else {
                log.warn("No JWT token found in SecurityContext; outgoing Feign call will be unauthenticated.");
            }
        };
    }
}
