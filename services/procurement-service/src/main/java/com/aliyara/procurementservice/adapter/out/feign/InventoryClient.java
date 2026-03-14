package com.aliyara.procurementservice.adapter.out.feign;

import com.aliyara.procurementservice.application.dto.feign.ProductResponse;
import com.aliyara.procurementservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "inventory-service", configuration = FeignConfig.class)
public interface InventoryClient {

    @GetMapping("/api/v1/products/{id}")
    ProductResponse getProduct(@PathVariable("id") UUID id);
}
