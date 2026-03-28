package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.stock.StockHistoryResponse;
import com.aliyara.inventoryservice.application.mapper.StockDtoMapper;
import com.aliyara.inventoryservice.application.port.in.StockHistoryUseCase;
import com.aliyara.inventoryservice.domain.port.StockHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockHistoryService implements StockHistoryUseCase {

    private final StockHistoryRepository stockHistoryRepository;
    private final StockDtoMapper stockDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StockHistoryResponse> getHistoryByProductId(UUID productId) {
        return stockHistoryRepository.findByProductId(productId)
                .stream()
                .map(stockDtoMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }
}
