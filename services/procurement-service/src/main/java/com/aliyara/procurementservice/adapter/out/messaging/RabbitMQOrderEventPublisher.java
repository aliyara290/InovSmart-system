package com.aliyara.procurementservice.adapter.out.messaging;

import com.aliyara.procurementservice.application.event.StockIncreaseEvent;
import com.aliyara.procurementservice.application.port.out.OrderEventPublisherPort;
import com.aliyara.procurementservice.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderEventPublisher implements OrderEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishStockIncreaseEvent(StockIncreaseEvent event) {
        log.info("Publishing StockIncreaseEvent to exchange '{}' with routing key '{}': {}",
                RabbitMQConfig.STOCK_INCREASE_EXCHANGE,
                RabbitMQConfig.STOCK_INCREASE_ROUTING_KEY,
                event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_INCREASE_EXCHANGE,
                RabbitMQConfig.STOCK_INCREASE_ROUTING_KEY,
                event);
        log.info("Successfully published StockIncreaseEvent for orderId={}, productId={}, qty={}",
                event.orderId(), event.productId(), event.quantity());
    }
}
