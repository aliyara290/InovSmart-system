package com.aliyara.companyservice.adapters.out.keycloak.client;

import com.aliyara.companyservice.adapters.out.keycloak.config.KeycloakTokenClientConfig;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakTokenResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "keycloak-token-client",
        url = "${keycloak.base-url}",
        configuration = KeycloakTokenClientConfig.class
)
public interface KeycloakTokenClient {

    @RequestLine("POST /realms/{realm}/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    KeycloakTokenResponse getAdminToken(
            @Param("realm") String realm,
            @Param("grant_type") String grantType,
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret
    );

    @RequestLine("POST /realms/{realm}/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    KeycloakTokenResponse getUserToken(
            @Param("realm") String realm,
            @Param("grant_type") String grantType,
            @Param("username") String username,
            @Param("password") String password,
            @Param("client_id") String clientId
//            @Param("client_secret") String clientSecret
    );
}
