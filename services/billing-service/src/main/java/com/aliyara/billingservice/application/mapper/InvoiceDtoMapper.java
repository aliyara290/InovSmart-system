package com.aliyara.billingservice.application.mapper;

import com.aliyara.billingservice.application.dto.invoice.InvoiceLineRequest;
import com.aliyara.billingservice.application.dto.invoice.InvoiceLineResponse;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceDtoMapper {

    InvoiceLineResponse toLineResponse(InvoiceLine line);

    List<InvoiceLineResponse> toLineResponseList(List<InvoiceLine> lines);

    InvoiceResponse toResponse(Invoice invoice);

    default List<InvoiceLine> toDomainLines(List<InvoiceLineRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(r -> new InvoiceLine.Builder()
                        .productName(r.getProductName())
                        .description(r.getDescription())
                        .quantity(r.getQuantity())
                        .unitPrice(r.getUnitPrice())
                        .taxRate(r.getTaxRate())
                        .build())
                .toList();
    }
}
