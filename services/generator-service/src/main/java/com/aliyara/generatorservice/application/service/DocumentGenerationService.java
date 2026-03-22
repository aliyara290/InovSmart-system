package com.aliyara.generatorservice.application.service;

import com.aliyara.generatorservice.application.dto.*;
import com.aliyara.generatorservice.application.port.in.GenerateDocumentUseCase;
import com.aliyara.generatorservice.application.port.out.DocumentEventPublisherPort;
import com.aliyara.generatorservice.application.port.out.PdfConverterPort;
import com.aliyara.generatorservice.application.port.out.StoragePort;
import com.aliyara.generatorservice.application.port.out.TemplateEnginePort;
import com.aliyara.generatorservice.domain.exception.InvalidPayloadException;
import com.aliyara.generatorservice.domain.model.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentGenerationService implements GenerateDocumentUseCase {

    private final TemplateEnginePort templateEnginePort;
    private final PdfConverterPort pdfConverterPort;
    private final StoragePort storagePort;
    private final DocumentEventPublisherPort documentEventPublisherPort;

    @Override
    public void generateDocument(GenerateDocumentCommand command) {
        log.info("Starting document generation for documentId: {}, type: {}", command.getDocumentId(), command.getType());

        try {
            validatePayload(command);

            String templateName = selectTemplate(command.getType());

            Map<String, Object> variables = buildTemplateVariables(command);
            String html = templateEnginePort.render(templateName, variables);
            log.debug("HTML rendered for documentId: {}", command.getDocumentId());

            byte[] pdfBytes = pdfConverterPort.convertHtmlToPdf(html);
            log.debug("PDF generated for documentId: {}, size: {} bytes", command.getDocumentId(), pdfBytes.length);

            String s3Key = buildS3Key(command.getType(), command.getDocumentId());
            String documentUrl = storagePort.upload(s3Key, pdfBytes, "application/pdf");
            log.info("PDF uploaded for documentId: {}, url: {}", command.getDocumentId(), documentUrl);

            documentEventPublisherPort.publishDocumentGenerated(
                    new DocumentGeneratedEvent(command.getDocumentId(), command.getType(), documentUrl)
            );

        } catch (Exception e) {
            log.error("Document generation failed for documentId: {}", command.getDocumentId(), e);
            documentEventPublisherPort.publishDocumentFailed(
                    new DocumentFailedEvent(command.getDocumentId(), command.getType(), e.getMessage())
            );
        }
    }

    private void validatePayload(GenerateDocumentCommand command) {
        if (command.getDocumentId() == null || command.getDocumentId().isBlank()) {
            throw new InvalidPayloadException("documentId is required");
        }
        if (command.getType() == null) {
            throw new InvalidPayloadException("document type is required");
        }
        if (command.getCompany() == null) {
            throw new InvalidPayloadException("company data is required");
        }
        if (command.getItems() == null || command.getItems().isEmpty()) {
            throw new InvalidPayloadException("items must not be empty");
        }
        if (command.getTotals() == null) {
            throw new InvalidPayloadException("totals data is required");
        }
    }

    private String selectTemplate(DocumentType type) {
        return switch (type) {
            case QUOTE -> "quote";
            case INVOICE -> "invoice";
        };
    }

    private Map<String, Object> buildTemplateVariables(GenerateDocumentCommand command) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentId", command.getDocumentId());
        variables.put("type", command.getType().name());
        variables.put("company", command.getCompany());
        variables.put("customer", command.getCustomer());
        variables.put("items", command.getItems());
        variables.put("totals", command.getTotals());
        return variables;
    }

    private String buildS3Key(DocumentType type, String documentId) {
        return switch (type) {
            case QUOTE -> "quotes/" + documentId + ".pdf";
            case INVOICE -> "invoices/" + documentId + ".pdf";
        };
    }
}
