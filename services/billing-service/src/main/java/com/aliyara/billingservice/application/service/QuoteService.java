package com.aliyara.billingservice.application.service;

import com.aliyara.billingservice.application.dto.document.*;
import com.aliyara.billingservice.application.dto.quote.*;
import com.aliyara.billingservice.application.mapper.QuoteDtoMapper;
import com.aliyara.billingservice.application.port.in.QuoteUseCase;
import com.aliyara.billingservice.application.port.out.CompanyServicePort;
import com.aliyara.billingservice.application.port.out.DocumentEventPublisherPort;
import com.aliyara.billingservice.application.port.out.InventoryServicePort;
import com.aliyara.billingservice.application.port.out.QuoteRepositoryPort;
import com.aliyara.billingservice.domain.exception.ProductNotFoundException;
import com.aliyara.billingservice.domain.exception.QuoteNotFoundException;
import com.aliyara.billingservice.domain.exception.StockReservationException;
import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import com.aliyara.billingservice.infrastructure.config.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService implements QuoteUseCase {

    private final QuoteRepositoryPort quoteRepositoryPort;
    private final InventoryServicePort inventoryServicePort;
    private final CompanyServicePort companyServicePort;
    private final DocumentEventPublisherPort documentEventPublisherPort;
    private final QuoteDtoMapper quoteDtoMapper;

    @Override
    @Transactional
    public QuoteResponse createQuote(CreateQuoteRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        log.info("Creating quote for tenant: {}", tenantId);

        validateProductsExist(request.getLines());

        List<QuoteLine> domainLines = request.getLines().stream()
                .map(this::toDomainLine)
                .collect(Collectors.toList());

        Quote quote = new Quote.Builder()
                .tenantId(tenantId)
                .clientId(request.getClientId())
                .taxRate(request.getTaxRate())
                .lines(domainLines)
                .build();

        Quote saved = quoteRepositoryPort.save(quote);
        return quoteDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuoteResponse updateQuote(UUID quoteId, UpdateQuoteRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);

        validateProductsExist(request.getLines());

        List<QuoteLine> newLines = request.getLines().stream()
                .map(this::toDomainLine)
                .collect(Collectors.toList());

        quote.replaceLines(newLines);

        Quote saved = quoteRepositoryPort.save(quote);
        return quoteDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteResponse getQuote(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);
        return quoteDtoMapper.toResponse(quote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuoteResponse> getAllQuotes() {
        String tenantId = TenantContextHolder.getTenantId();
        return quoteRepositoryPort.findAllByTenantId(tenantId).stream()
                .map(quoteDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuoteResponse sendQuote(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);

        quote.send();

        Quote saved = quoteRepositoryPort.save(quote);
        return quoteDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuoteResponse acceptQuote(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);

        quote.accept();

        log.debug("quote: {}", quote);
        List<QuoteLine> reservedLines = new ArrayList<>();
        try {
            for (QuoteLine line : quote.getLines()) {
                inventoryServicePort.reserveStock(line.getProductId(), line.getQuantity(), quote.getId());
                reservedLines.add(line);
                log.debug("line ++++++ = {}", line);
                log.debug("reservedLines ++++++ = {}", reservedLines);
            }
        } catch (Exception e) {
            log.error("Stock reservation failed for quote {}. Rolling back reservations.", quoteId, e);
            for (QuoteLine reserved : reservedLines) {
                try {
                    inventoryServicePort.releaseReservation(reserved.getProductId(), reserved.getQuantity(), quote.getId());
                } catch (Exception rollbackEx) {
                    log.error("Failed to rollback reservation for product {}", reserved.getProductId(), rollbackEx);
                }
            }
            throw new StockReservationException("Stock reservation failed for quote " + quoteId, e);
        }

        Quote saved = quoteRepositoryPort.save(quote);
        return quoteDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuoteResponse rejectQuote(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);

        boolean wasAccepted = quote.getStatus() == com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus.ACCEPTED;

        quote.reject();

        if (wasAccepted) {
            for (QuoteLine line : quote.getLines()) {
                try {
                    inventoryServicePort.releaseReservation(line.getProductId(), line.getQuantity(), quote.getId());
                } catch (Exception e) {
                    log.error("Failed to release reservation for product {} on quote {}", line.getProductId(), quoteId, e);
                }
            }
        }

        Quote saved = quoteRepositoryPort.save(quote);
        return quoteDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuoteResponse generateQuoteDocument(UUID quoteId) {
        String tenantId = TenantContextHolder.getTenantId();
        log.info("Generating document for quote {} tenant {}", quoteId, tenantId);

        Quote quote = findQuoteByIdAndTenant(quoteId, tenantId);

        CompanySnapshot companySnapshot = companyServicePort.getCompanySnapshot(tenantId);

        CustomerSnapshot customerSnapshot = quote.getCustomerSnapshot();
        if (customerSnapshot == null) {
            customerSnapshot = new CustomerSnapshot(null, null, null, null);
        }

        quote.startDocumentGeneration(companySnapshot, customerSnapshot);
        Quote saved = quoteRepositoryPort.save(quote);

        GenerateDocumentEvent event = buildGenerateEvent(saved, "QUOTE");
        documentEventPublisherPort.publishGenerateDocument(event);

        return quoteDtoMapper.toResponse(saved);
    }

    private GenerateDocumentEvent buildGenerateEvent(Quote quote, String type) {
        CompanySnapshotDto companyDto = new CompanySnapshotDto(
                quote.getCompanySnapshot().getName(),
                quote.getCompanySnapshot().getEmail(),
                quote.getCompanySnapshot().getPhone(),
                quote.getCompanySnapshot().getAddress()
        );

        CustomerSnapshotDto customerDto = null;
        if (quote.getCustomerSnapshot() != null) {
            customerDto = new CustomerSnapshotDto(
                    quote.getCustomerSnapshot().getName(),
                    quote.getCustomerSnapshot().getEmail(),
                    quote.getCustomerSnapshot().getPhone(),
                    quote.getCustomerSnapshot().getAddress()
            );
        }

        List<LineItemDto> items = quote.getLines().stream()
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
                quote.getSubtotal(),
                quote.getTaxRate(),
                quote.getTaxAmount(),
                quote.getTotal()
        );

        return new GenerateDocumentEvent(
                quote.getId().toString(),
                type,
                companyDto,
                customerDto,
                items,
                totals
        );
    }

    private void validateProductsExist(List<QuoteLineRequest> lines) {
        for (QuoteLineRequest line : lines) {
            if (!inventoryServicePort.productExists(line.getProductId())) {
                throw new ProductNotFoundException("Product not found: " + line.getProductId());
            }
        }
    }

    private QuoteLine toDomainLine(QuoteLineRequest request) {
        String productName = inventoryServicePort.getProductName(request.getProductId());
        if (productName == null || productName.isBlank()) {
            productName = request.getProductId().toString();
        }
        return new QuoteLine.Builder()
                .productId(request.getProductId())
                .productName(productName)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .build();
    }

    private Quote findQuoteByIdAndTenant(UUID quoteId, String tenantId) {
        Quote quote = quoteRepositoryPort.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + quoteId));
        if (!quote.getTenantId().equals(tenantId)) {
            throw new QuoteNotFoundException("Quote not found: " + quoteId);
        }
        return quote;
    }
}
