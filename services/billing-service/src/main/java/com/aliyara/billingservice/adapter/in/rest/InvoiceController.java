package com.aliyara.billingservice.adapter.in.rest;

import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.application.port.in.InvoiceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createInvoice(
            @Valid @RequestBody CreateInvoiceRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String tenantId = jwt.getClaimAsString("tenantId");
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID not found in token");
        }

        request.setTenantId(tenantId);
        return invoiceUseCase.createInvoice(request);
    }

    @PostMapping("/from-quote/{quoteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createInvoiceFromQuote(@PathVariable UUID quoteId) {
        return invoiceUseCase.createInvoiceFromQuote(quoteId);
    }

    @GetMapping("/{id}")
    public InvoiceResponse getInvoice(@PathVariable UUID id) {
        return invoiceUseCase.getInvoice(id);
    }

    @GetMapping
    public List<InvoiceResponse> getInvoicesByTenant(@RequestParam String tenantId) {
        return invoiceUseCase.getInvoicesByTenant(tenantId);
    }

    @PostMapping("/{id}/send")
    public InvoiceResponse sendInvoice(@PathVariable UUID id) {
        return invoiceUseCase.sendInvoice(id);
    }
}
