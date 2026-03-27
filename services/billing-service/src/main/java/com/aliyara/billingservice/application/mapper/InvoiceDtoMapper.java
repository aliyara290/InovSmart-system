package com.aliyara.billingservice.application.mapper;

import com.aliyara.billingservice.application.dto.invoice.InvoiceLineResponse;
import com.aliyara.billingservice.application.dto.invoice.InvoiceResponse;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceDtoMapper {

    InvoiceResponse toResponse(Invoice invoice);

    InvoiceLineResponse toLineResponse(InvoiceLine line);

    List<InvoiceLineResponse> toLineResponses(List<InvoiceLine> lines);
}
