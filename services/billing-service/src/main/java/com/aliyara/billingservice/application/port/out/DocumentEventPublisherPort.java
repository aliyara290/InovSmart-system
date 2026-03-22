package com.aliyara.billingservice.application.port.out;

import com.aliyara.billingservice.application.dto.document.GenerateDocumentEvent;

public interface DocumentEventPublisherPort {

    void publishGenerateDocument(GenerateDocumentEvent event);
}
