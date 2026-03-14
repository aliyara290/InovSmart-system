package com.aliyara.procurementservice.application.port.out;

import java.util.UUID;

public interface InventoryClientPort {
    boolean productExists(UUID productId);
}
