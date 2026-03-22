package com.aliyara.generatorservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.document-generated}")
    private String documentGeneratedTopic;

    @Value("${app.kafka.topics.document-failed}")
    private String documentFailedTopic;

    @Bean
    public NewTopic documentGeneratedTopic() {
        return TopicBuilder.name(documentGeneratedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic documentFailedTopic() {
        return TopicBuilder.name(documentFailedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
