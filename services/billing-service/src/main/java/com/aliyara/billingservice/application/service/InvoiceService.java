package com.aliyara.billingservice.application.service;

import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.application.mapper.InvoiceDtoMapper;
import com.aliyara.billingservice.application.port.in.InvoiceUseCase;
import com.aliyara.billingservice.domain.exception.InvoiceNotFoundException;
import com.aliyara.billingservice.domain.exception.InvalidStatusTransitionException;
import com.aliyara.billingservice.domain.exception.QuoteNotFoundException;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus;
import com.aliyara.billingservice.domain.port.InvoiceRepository;
import com.aliyara.billingservice.domain.port.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService implements InvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final QuoteRepository quoteRepository;
    private final InvoiceDtoMapper invoiceMapper;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        log.info("Creating invoice for tenantId={}, number={}", request.getTenantId(), request.getInvoiceNumber());

        List<InvoiceLine> domainLines = invoiceMapper.toDomainLines(request.getLines());

        Invoice invoice = new Invoice.Builder()
                .tenantId(request.getTenantId())
                .title(request.getTitle())
                .invoiceNumber(request.getInvoiceNumber())
                .lines(domainLines)
                .build();

        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoiceFromQuote(UUID quoteId) {
        log.info("Creating invoice from quote id={}", quoteId);

        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found with id: " + quoteId));

        if (quote.getStatus() != QuoteStatus.ACCEPTED) {
            throw new InvalidStatusTransitionException("Cannot create invoice from quote " + quoteId
                    + ". Quote must be ACCEPTED, but is currently " + quote.getStatus() + ".");
        }

        List<InvoiceLine> invoiceLines = quote.getLines().stream()
                .map(ql -> new InvoiceLine.Builder()
                        .productName(ql.getProductName())
                        .description(ql.getDescription())
                        .quantity(ql.getQuantity())
                        .unitPrice(ql.getUnitPrice())
                        .taxRate(ql.getTaxRate())
                        .build())
                .collect(Collectors.toList());

        Invoice invoice = new Invoice.Builder()
                .tenantId(quote.getTenantId())
                .quoteId(quote.getId())
                .title("Invoice for " + quote.getTitle())
                .invoiceNumber("INV-FROM-" + quote.getId().toString().substring(0, 8).toUpperCase())
                .lines(invoiceLines)
                .build();

        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + id));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByTenant(String tenantId) {
        return invoiceRepository.findAllByTenantId(tenantId)
                .stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceResponse sendInvoice(UUID id) {
        log.info("Transitioning invoice id={} to SENT", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + id));
        invoice.markAsSent();
        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }
}
