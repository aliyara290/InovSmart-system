package com.aliyara.billingservice.adapter.in.messaging;

import com.aliyara.billingservice.application.dto.document.DocumentFailedEvent;
import com.aliyara.billingservice.application.dto.document.DocumentGeneratedEvent;
import com.aliyara.billingservice.application.port.in.DocumentGenerationCallbackUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentEventConsumer {

    private final DocumentGenerationCallbackUseCase callbackUseCase;

    @KafkaListener(topics = "${app.kafka.topics.document-generated}", groupId = "${spring.kafka.consumer.group-id}")
    public void onDocumentGenerated(DocumentGeneratedEvent event) {
        log.info("Received document.generated event for documentId: {}, type: {}", event.getDocumentId(), event.getType());
        callbackUseCase.onDocumentGenerated(event.getDocumentId(), event.getType(), event.getDocumentUrl());
    }

    @KafkaListener(topics = "${app.kafka.topics.document-failed}", groupId = "${spring.kafka.consumer.group-id}")
    public void onDocumentFailed(DocumentFailedEvent event) {
        log.warn("Received document.failed event for documentId: {}, type: {}, reason: {}", event.getDocumentId(), event.getType(), event.getReason());
        callbackUseCase.onDocumentFailed(event.getDocumentId(), event.getType());
    }
}
