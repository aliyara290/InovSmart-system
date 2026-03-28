package com.aliyara.companyservice.infrastructure.filter;

import com.aliyara.companyservice.infrastructure.config.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("SecurityContextHolder /////" );
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                String tenantIdStr = jwt.getClaimAsString("tenantId");
                log.debug("user is is: {}", tenantIdStr);
                if (tenantIdStr != null) {
                    UUID tenantId = UUID.fromString(tenantIdStr);
                    log.info("tenantId: {}", tenantId );
                    TenantContextHolder.setTenantId(tenantId);
                }
            } else {
                log.error("user is is null");
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
