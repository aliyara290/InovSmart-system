package com.aliyara.billingservice.adapter.out.messaging;

import com.aliyara.billingservice.application.dto.document.GenerateDocumentEvent;
import com.aliyara.billingservice.application.port.out.DocumentEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDocumentEventPublisher implements DocumentEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.document-generate}")
    private String documentGenerateTopic;

    @Override
    public void publishGenerateDocument(GenerateDocumentEvent event) {
        log.info("Publishing document.generate event for documentId: {}, type: {}", event.getDocumentId(), event.getType());
        kafkaTemplate.send(documentGenerateTopic, event.getDocumentId(), event);
    }
}
