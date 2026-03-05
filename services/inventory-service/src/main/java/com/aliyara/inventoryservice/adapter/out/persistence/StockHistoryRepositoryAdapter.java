package com.aliyara.inventoryservice.adapter.out.persistence;

import com.aliyara.inventoryservice.adapter.out.persistence.jpa.StockHistoryJpaRepository;
import com.aliyara.inventoryservice.adapter.out.persistence.mapper.StockHistoryPersistenceMapper;
import com.aliyara.inventoryservice.domain.port.StockHistoryRepository;
import com.aliyara.inventoryservice.domain.stock.StockHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockHistoryRepositoryAdapter implements StockHistoryRepository {

    private final StockHistoryJpaRepository stockHistoryJpaRepository;
    private final StockHistoryPersistenceMapper stockHistoryPersistenceMapper;

    @Override
    public StockHistory save(StockHistory stockHistory) {
        var entity = stockHistoryPersistenceMapper.toEntity(stockHistory);
        var saved = stockHistoryJpaRepository.save(entity);
        return stockHistoryPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<StockHistory> findAllByProductId(UUID productId) {
        return stockHistoryJpaRepository.findAllByProductId(productId)
                .stream()
                .map(stockHistoryPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockHistory> findAllByTenantId(String tenantId) {
        return stockHistoryJpaRepository.findAllByTenantId(tenantId)
                .stream()
                .map(stockHistoryPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
