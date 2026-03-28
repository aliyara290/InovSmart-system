package com.aliyara.inventoryservice.adapter.out.persistence;

import com.aliyara.inventoryservice.adapter.out.persistence.jpa.StockJpaRepository;
import com.aliyara.inventoryservice.adapter.out.persistence.mapper.StockPersistenceMapper;
import com.aliyara.inventoryservice.domain.port.StockRepository;
import com.aliyara.inventoryservice.domain.model.stock.Stock;
import com.aliyara.inventoryservice.domain.model.stock.enums.StockStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepository {

    private final StockJpaRepository stockJpaRepository;
    private final StockPersistenceMapper stockPersistenceMapper;

    @Override
    public Stock save(Stock stock) {
        var entity = stockPersistenceMapper.toEntity(stock);
        var saved = stockJpaRepository.save(entity);
        return stockPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Stock> findByProductId(UUID productId) {
        return stockJpaRepository.findByProductId(productId)
                .map(stockPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Stock> findByProductIdAndTenantId(UUID productId, String tenantId) {
        return stockJpaRepository.findByProductIdAndTenantId(productId, tenantId)
                .map(stockPersistenceMapper::toDomain);
    }

    @Override
    public List<Stock> findAllByTenantId(String tenantId) {
        return stockJpaRepository.findAllByTenantId(tenantId)
                .stream()
                .map(stockPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findByTenantIdAndStatus(String tenantId, StockStatus status) {
        return stockJpaRepository.findByTenantIdAndStatus(tenantId, status)
                .stream()
                .map(stockPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findLowStockByTenantId(String tenantId) {
        return stockJpaRepository.findLowStockByTenantId(tenantId)
                .stream()
                .map(stockPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByProductId(UUID productId) {
        stockJpaRepository.deleteByProductId(productId);
    }
}
