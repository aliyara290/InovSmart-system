package com.aliyara.billingservice.application.port.in;

import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;

import java.util.List;
import java.util.UUID;

public interface InvoiceUseCase {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse createInvoiceFromQuote(UUID quoteId);

    InvoiceResponse getInvoice(UUID invoiceId);

    List<InvoiceResponse> getAllInvoices();

    InvoiceResponse sendInvoice(UUID invoiceId);

    InvoiceResponse markInvoicePaid(UUID invoiceId);

    InvoiceResponse cancelInvoice(UUID invoiceId);

    InvoiceResponse generateInvoiceDocument(UUID invoiceId);


    List<InvoiceResponse> getAllInvoicesByStatusPaid();
}
