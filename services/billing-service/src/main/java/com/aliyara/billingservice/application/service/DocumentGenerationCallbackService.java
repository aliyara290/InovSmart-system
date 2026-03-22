package com.aliyara.billingservice.application.service;

import com.aliyara.billingservice.application.port.in.DocumentGenerationCallbackUseCase;
import com.aliyara.billingservice.application.port.out.InvoiceRepositoryPort;
import com.aliyara.billingservice.application.port.out.QuoteRepositoryPort;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.quote.Quote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentGenerationCallbackService implements DocumentGenerationCallbackUseCase {

    private final QuoteRepositoryPort quoteRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;

    @Override
    @Transactional
    public void onDocumentGenerated(String documentId, String type, String documentUrl) {
        UUID id = UUID.fromString(documentId);
        if ("QUOTE".equalsIgnoreCase(type)) {
            Quote quote = quoteRepositoryPort.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Quote not found for callback: " + documentId));
            quote.markDocumentReady(documentUrl);
            quoteRepositoryPort.save(quote);
            log.info("Quote {} document marked READY, url: {}", documentId, documentUrl);
        } else if ("INVOICE".equalsIgnoreCase(type)) {
            Invoice invoice = invoiceRepositoryPort.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Invoice not found for callback: " + documentId));
            invoice.markDocumentReady(documentUrl);
            invoiceRepositoryPort.save(invoice);
            log.info("Invoice {} document marked READY, url: {}", documentId, documentUrl);
        } else {
            log.warn("Unknown document type in generated callback: {}", type);
        }
    }

    @Override
    @Transactional
    public void onDocumentFailed(String documentId, String type) {
        UUID id = UUID.fromString(documentId);
        if ("QUOTE".equalsIgnoreCase(type)) {
            Quote quote = quoteRepositoryPort.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Quote not found for callback: " + documentId));
            quote.markDocumentFailed();
            quoteRepositoryPort.save(quote);
            log.warn("Quote {} document marked FAILED", documentId);
        } else if ("INVOICE".equalsIgnoreCase(type)) {
            Invoice invoice = invoiceRepositoryPort.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Invoice not found for callback: " + documentId));
            invoice.markDocumentFailed();
            invoiceRepositoryPort.save(invoice);
            log.warn("Invoice {} document marked FAILED", documentId);
        } else {
            log.warn("Unknown document type in failed callback: {}", type);
        }
    }
}
