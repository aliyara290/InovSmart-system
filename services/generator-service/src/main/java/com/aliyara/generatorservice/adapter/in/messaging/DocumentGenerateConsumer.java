package com.aliyara.generatorservice.adapter.in.messaging;

import com.aliyara.generatorservice.application.dto.GenerateDocumentCommand;
import com.aliyara.generatorservice.application.port.in.GenerateDocumentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentGenerateConsumer {

    private final GenerateDocumentUseCase generateDocumentUseCase;

    @KafkaListener(topics = "${app.kafka.topics.document-generate}", groupId = "${spring.kafka.consumer.group-id}")
    public void onDocumentGenerate(GenerateDocumentCommand command) {
        log.info("Received document.generate event for documentId: {}, type: {}", command.getDocumentId(), command.getType());
        try {
            generateDocumentUseCase.generateDocument(command);
        } catch (Exception e) {
            log.error("Failed to generate document for documentId: {}, type: {}", command.getDocumentId(), command.getType(), e);
            throw e;
        }
    }
}
