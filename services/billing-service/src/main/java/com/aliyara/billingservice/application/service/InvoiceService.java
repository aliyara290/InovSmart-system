package com.aliyara.billingservice.application.service;

import com.aliyara.billingservice.application.dto.document.*;
import com.aliyara.billingservice.application.dto.invoice.CreateInvoiceRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceLineRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.application.mapper.InvoiceDtoMapper;
import com.aliyara.billingservice.application.port.in.InvoiceUseCase;
import com.aliyara.billingservice.application.port.out.CompanyServicePort;
import com.aliyara.billingservice.application.port.out.DocumentEventPublisherPort;
import com.aliyara.billingservice.application.port.out.InventoryServicePort;
import com.aliyara.billingservice.application.port.out.InvoiceRepositoryPort;
import com.aliyara.billingservice.application.port.out.QuoteRepositoryPort;
import com.aliyara.billingservice.domain.exception.*;
import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus;
import com.aliyara.billingservice.infrastructure.config.TenantContextHolder;
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

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final QuoteRepositoryPort quoteRepositoryPort;
    private final InventoryServicePort inventoryServicePort;
    private final CompanyServicePort companyServicePort;
    private final DocumentEventPublisherPort documentEventPublisherPort;
    private final InvoiceDtoMapper invoiceDtoMapper;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        log.info("Creating manual invoice for tenant: {}", tenantId);

        validateProductsExist(request.getLines());

        List<InvoiceLine> domainLines = request.getLines().stream()
                .map(this::toDomainLine)
                .collect(Collectors.toList());

        Invoice invoice = new Invoice.Builder()
                .tenantId(tenantId)
                .clientId(request.getClientId())
                .taxRate(request.getTaxRate())
                .lines(domainLines)
                .build();

        for (InvoiceLine line : domainLines) {
            try {
                int currentStock = inventoryServicePort.getAvailableStock(line.getProductId());
                int newTotal = currentStock - line.getQuantity();
                inventoryServicePort.adjustStock(line.getProductId(), newTotal, invoice.getId());
            } catch (Exception e) {
                log.error("Failed to adjust stock for product {} during manual invoice creation", line.getProductId(), e);
                throw new StockReservationException("Stock adjustment failed for product " + line.getProductId(), e);
            }
        }

        Invoice saved = invoiceRepositoryPort.save(invoice);
        return invoiceDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoiceFromQuote(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        log.info("Creating invoice from quote {} for tenant: {}", quoteId, tenantId);

        if (invoiceRepositoryPort.existsByQuoteIdAndTenantId(quoteId, tenantId)) {
            throw new DuplicateInvoiceException("Invoice already exists for quote: " + quoteId);
        }

        Quote quote = quoteRepositoryPort.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + quoteId));

        if (!quote.getTenantId().equals(tenantId)) {
            throw new QuoteNotFoundException("Quote not found: " + quoteId);
        }

        if (quote.getStatus() != QuoteStatus.ACCEPTED) {
            throw new InvalidStateTransitionException(
                    "Invoice can only be created from an ACCEPTED quote. Current status: " + quote.getStatus());
        }

        List<InvoiceLine> invoiceLines = quote.getLines().stream()
                .map(ql -> new InvoiceLine.Builder()
                        .productId(ql.getProductId())
                        .productName(ql.getProductName())
                        .quantity(ql.getQuantity())
                        .unitPrice(ql.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        Invoice invoice = new Invoice.Builder()
                .tenantId(tenantId)
                .clientId(quote.getClientId())
                .quoteId(quoteId)
                .taxRate(quote.getTaxRate())
                .lines(invoiceLines)
                .build();

        for (QuoteLine line : quote.getLines()) {
            try {
                int currentStock = inventoryServicePort.getAvailableStock(line.getProductId());
                int newTotal = currentStock - line.getQuantity();
                inventoryServicePort.adjustStock(line.getProductId(), newTotal, invoice.getId());
            } catch (Exception e) {
                log.error("Failed to adjust stock for product {} during invoice creation from quote {}", line.getProductId(), quoteId, e);
                throw new StockReservationException("Stock adjustment failed for product " + line.getProductId(), e);
            }
        }

        Invoice saved = invoiceRepositoryPort.save(invoice);
        return invoiceDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(UUID invoiceId) {
        String tenantId = TenantContextHolder.getTenantId();
        log.debug("TENANTID: {}", tenantId);
        Invoice invoice = findInvoiceByIdAndTenant(invoiceId, tenantId);
        return invoiceDtoMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        String tenantId = TenantContextHolder.getTenantId();
        return invoiceRepositoryPort.findAllByTenantId(tenantId).stream()
                .map(invoiceDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceResponse sendInvoice(UUID invoiceId) {
        String tenantId = TenantContextHolder.getTenantId();
        Invoice invoice = findInvoiceByIdAndTenant(invoiceId, tenantId);

        invoice.markSent();

        Invoice saved = invoiceRepositoryPort.save(invoice);
        return invoiceDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse markInvoicePaid(UUID invoiceId) {
        String tenantId = TenantContextHolder.getTenantId();
        Invoice invoice = findInvoiceByIdAndTenant(invoiceId, tenantId);

        invoice.markPaid();

        Invoice saved = invoiceRepositoryPort.save(invoice);
        return invoiceDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(UUID invoiceId) {
        String tenantId = TenantContextHolder.getTenantId();
        Invoice invoice = findInvoiceByIdAndTenant(invoiceId, tenantId);

        invoice.cancel();

        Invoice saved = invoiceRepositoryPort.save(invoice);
        return invoiceDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse generateInvoiceDocument(UUID invoiceId) {
        String tenantId = TenantContextHolder.getTenantId();
        log.info("Generating document for invoice {} tenant {}", invoiceId, tenantId);

        Invoice invoice = findInvoiceByIdAndTenant(invoiceId, tenantId);

        CompanySnapshot companySnapshot = companyServicePort.getCompanySnapshot(tenantId);

        CustomerSnapshot customerSnapshot = invoice.getCustomerSnapshot();
        if (customerSnapshot == null) {
            customerSnapshot = new CustomerSnapshot(null, null, null, null);
        }

        invoice.startDocumentGeneration(companySnapshot, customerSnapshot);
        Invoice saved = invoiceRepositoryPort.save(invoice);

        GenerateDocumentEvent event = buildGenerateEvent(saved, "INVOICE");
        documentEventPublisherPort.publishGenerateDocument(event);

        return invoiceDtoMapper.toResponse(saved);
    }

    private GenerateDocumentEvent buildGenerateEvent(Invoice invoice, String type) {
        CompanySnapshotDto companyDto = new CompanySnapshotDto(
                invoice.getCompanySnapshot().getName(),
                invoice.getCompanySnapshot().getEmail(),
                invoice.getCompanySnapshot().getPhone(),
                invoice.getCompanySnapshot().getAddress()
        );

        CustomerSnapshotDto customerDto = null;
        if (invoice.getCustomerSnapshot() != null) {
            customerDto = new CustomerSnapshotDto(
                    invoice.getCustomerSnapshot().getName(),
                    invoice.getCustomerSnapshot().getEmail(),
                    invoice.getCustomerSnapshot().getPhone(),
                    invoice.getCustomerSnapshot().getAddress()
            );
        }

        List<LineItemDto> items = invoice.getLines().stream()
                .map(line -> {
                    String description = line.getProductName();
                    if (description == null || description.isBlank()) {
                        description = line.getProductId().toString();
                    }
                    return new LineItemDto(
                            line.getProductId().toString(),
                            description,
                            line.getQuantity(),
                            line.getUnitPrice(),
                            line.getLineTotal()
                    );
                })
                .collect(Collectors.toList());

        TotalsDto totals = new TotalsDto(
                invoice.getSubtotal(),
                invoice.getTaxRate(),
                invoice.getTaxAmount(),
                invoice.getTotal()
        );

        return new GenerateDocumentEvent(
                invoice.getId().toString(),
                type,
                companyDto,
                customerDto,
                items,
                totals
        );
    }

    private void validateProductsExist(List<InvoiceLineRequest> lines) {
        for (InvoiceLineRequest line : lines) {
            if (!inventoryServicePort.productExists(line.getProductId())) {
                throw new ProductNotFoundException("Product not found: " + line.getProductId());
            }
        }
    }

    private InvoiceLine toDomainLine(InvoiceLineRequest request) {
        String productName = inventoryServicePort.getProductName(request.getProductId());
        if (productName == null || productName.isBlank()) {
            productName = request.getProductId().toString();
        }
        return new InvoiceLine.Builder()
                .productId(request.getProductId())
                .productName(productName)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .build();
    }

    private Invoice findInvoiceByIdAndTenant(UUID invoiceId, String tenantId) {
        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found: " + invoiceId));
        if (!invoice.getTenantId().equals(tenantId)) {
            throw new InvoiceNotFoundException("Invoice not found: " + invoiceId);
        }
        return invoice;
    }
}
