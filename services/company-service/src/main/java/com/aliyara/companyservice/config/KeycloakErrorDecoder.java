package com.aliyara.companyservice.config;

import com.aliyara.companyservice.domain.exception.KeycloakIntegrationException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class KeycloakErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 599) {
            return new KeycloakIntegrationException(
                    "Keycloak API error: " + response.status() + " " + response.reason());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
