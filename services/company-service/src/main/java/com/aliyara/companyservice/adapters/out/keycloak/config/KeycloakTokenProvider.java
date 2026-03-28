package com.aliyara.companyservice.adapters.out.keycloak.config;

import com.aliyara.companyservice.adapters.out.keycloak.client.KeycloakTokenClient;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class KeycloakTokenProvider {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    private final KeycloakTokenClient tokenClient;

    private String cachedToken;
    private Instant tokenExpiryTime;

    public KeycloakTokenProvider(KeycloakTokenClient tokenClient) {
        this.tokenClient = tokenClient;
    }

    public synchronized String getValidToken() {
        if (cachedToken == null || tokenExpiryTime == null || Instant.now().isAfter(tokenExpiryTime)) {
            refreshToken();
        }
        return cachedToken;
    }

    private void refreshToken() {
        try {
            log.info("Refreshing Keycloak admin token for realm: {}", realm);
            KeycloakTokenResponse response = tokenClient.getAdminToken(
                    realm,
                    "client_credentials",
                    clientId,
                    clientSecret
            );

            this.cachedToken = response.getAccessToken();
            this.tokenExpiryTime = Instant.now().plusSeconds(response.getExpiresIn() - 60);
            log.info("Keycloak admin token refreshed successfully, expires in {} seconds", response.getExpiresIn());
        } catch (Exception e) {
            log.error("Failed to obtain Keycloak admin token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to obtain Keycloak admin token: " + e.getMessage(), e);
        }
    }
}
