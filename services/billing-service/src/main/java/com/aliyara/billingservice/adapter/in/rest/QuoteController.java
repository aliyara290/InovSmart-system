package com.aliyara.billingservice.adapter.in.rest;

import com.aliyara.billingservice.application.dto.quote.CreateQuoteRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.application.port.in.QuoteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteUseCase quoteUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuoteResponse createQuote(
            @Valid @RequestBody CreateQuoteRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String tenantId = jwt.getClaimAsString("tenantId");
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID not found in token");
        }

        request.setTenantId(tenantId);
        return quoteUseCase.createQuote(request);
    }

    @GetMapping("/{id}")
    public QuoteResponse getQuote(@PathVariable UUID id) {
        return quoteUseCase.getQuote(id);
    }

    @GetMapping
    public List<QuoteResponse> getQuotesByTenant(@RequestParam String tenantId) {
        // In a real scenario, compare with JWT tenantId to ensure user has access.
        // Assuming API Gateway or SecurityConfig handles the basic role checks.
        return quoteUseCase.getQuotesByTenant(tenantId);
    }

    @PostMapping("/{id}/send")
    public QuoteResponse sendQuote(@PathVariable UUID id) {
        return quoteUseCase.sendQuote(id);
    }

    @PostMapping("/{id}/accept")
    public QuoteResponse acceptQuote(@PathVariable UUID id) {
        return quoteUseCase.acceptQuote(id);
    }
}
