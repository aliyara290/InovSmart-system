package com.aliyara.billingservice.adapter.in.rest;

import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.application.port.in.InvoiceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public InvoiceResponse createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return invoiceUseCase.createInvoice(request);
    }

    @PostMapping("/from-quote/{quoteId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public InvoiceResponse createInvoiceFromQuote(@PathVariable UUID quoteId) {
        return invoiceUseCase.createInvoiceFromQuote(quoteId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public InvoiceResponse getInvoice(@PathVariable UUID id) {
        return invoiceUseCase.getInvoice(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceUseCase.getAllInvoices();
    }

    @PutMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public InvoiceResponse sendInvoice(@PathVariable UUID id) {
        return invoiceUseCase.sendInvoice(id);
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public InvoiceResponse markInvoicePaid(@PathVariable UUID id) {
        return invoiceUseCase.markInvoicePaid(id);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public InvoiceResponse cancelInvoice(@PathVariable UUID id) {
        return invoiceUseCase.cancelInvoice(id);
    }

    @PostMapping("/{id}/generate")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public InvoiceResponse generateInvoiceDocument(@PathVariable UUID id) {
        return invoiceUseCase.generateInvoiceDocument(id);
    }
}
