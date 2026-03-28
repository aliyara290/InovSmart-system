package com.aliyara.billingservice.adapter.in.rest;

import com.aliyara.billingservice.application.dto.quote.CreateQuoteRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.application.dto.quote.UpdateQuoteRequest;
import com.aliyara.billingservice.application.port.in.QuoteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public QuoteResponse createQuote(@Valid @RequestBody CreateQuoteRequest request) {
        return quoteUseCase.createQuote(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public QuoteResponse updateQuote(@PathVariable UUID id,
                                     @Valid @RequestBody UpdateQuoteRequest request) {
        return quoteUseCase.updateQuote(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public QuoteResponse getQuote(@PathVariable UUID id) {
        return quoteUseCase.getQuote(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public List<QuoteResponse> getAllQuotes() {
        return quoteUseCase.getAllQuotes();
    }

    @PutMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public QuoteResponse sendQuote(@PathVariable UUID id) {
        return quoteUseCase.sendQuote(id);
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public QuoteResponse acceptQuote(@PathVariable UUID id) {
        return quoteUseCase.acceptQuote(id);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public QuoteResponse rejectQuote(@PathVariable UUID id) {
        return quoteUseCase.rejectQuote(id);
    }

    @PostMapping("/{id}/generate")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public QuoteResponse generateQuoteDocument(@PathVariable UUID id) {
        return quoteUseCase.generateQuoteDocument(id);
    }
}
