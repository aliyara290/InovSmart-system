package com.aliyara.billingservice.application.port.in;

import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;

import java.util.List;
import java.util.UUID;

public interface InvoiceUseCase {
    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse createInvoiceFromQuote(UUID quoteId);

    InvoiceResponse getInvoice(UUID id);

    List<InvoiceResponse> getInvoicesByTenant(String tenantId);

    InvoiceResponse sendInvoice(UUID id);
}
