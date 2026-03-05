package com.aliyara.companyservice.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Configuration
public class FeignConfig {

    private static final String CLIENT_REGISTRATION_ID = "keycloak";

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(CLIENT_REGISTRATION_ID)
                    .principal("company-service")
                    .build();

            var authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            if (authorizedClient != null) {
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                requestTemplate.header("Authorization", "Bearer " + accessToken.getTokenValue());
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KeycloakErrorDecoder();
    }
}
