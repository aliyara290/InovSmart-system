package com.aliyara.procurementservice.application.port.out;

import com.aliyara.procurementservice.application.event.StockIncreaseEvent;

public interface OrderEventPublisherPort {
    void publishStockIncreaseEvent(StockIncreaseEvent event);
}
