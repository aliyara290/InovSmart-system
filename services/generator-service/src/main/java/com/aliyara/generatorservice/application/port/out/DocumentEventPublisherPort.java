package com.aliyara.generatorservice.application.port.out;

import com.aliyara.generatorservice.application.dto.DocumentFailedEvent;
import com.aliyara.generatorservice.application.dto.DocumentGeneratedEvent;

public interface DocumentEventPublisherPort {

    void publishDocumentGenerated(DocumentGeneratedEvent event);

    void publishDocumentFailed(DocumentFailedEvent event);
}
