package com.aliyara.generatorservice.adapter.out.messaging;

import com.aliyara.generatorservice.application.dto.DocumentFailedEvent;
import com.aliyara.generatorservice.application.dto.DocumentGeneratedEvent;
import com.aliyara.generatorservice.application.port.out.DocumentEventPublisherPort;
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

    @Value("${app.kafka.topics.document-generated}")
    private String documentGeneratedTopic;

    @Value("${app.kafka.topics.document-failed}")
    private String documentFailedTopic;

    @Override
    public void publishDocumentGenerated(DocumentGeneratedEvent event) {
        log.info("Publishing document.generated event for documentId: {}", event.getDocumentId());
        kafkaTemplate.send(documentGeneratedTopic, event.getDocumentId(), event);
    }

    @Override
    public void publishDocumentFailed(DocumentFailedEvent event) {
        log.warn("Publishing document.failed event for documentId: {}, reason: {}", event.getDocumentId(), event.getReason());
        kafkaTemplate.send(documentFailedTopic, event.getDocumentId(), event);
    }
}
