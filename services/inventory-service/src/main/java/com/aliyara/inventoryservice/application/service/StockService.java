package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.stock.AdjustStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.ReserveStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;
import com.aliyara.inventoryservice.application.mapper.StockDtoMapper;
import com.aliyara.inventoryservice.application.port.in.StockUseCase;
import com.aliyara.inventoryservice.domain.exception.StockNotFoundException;
import com.aliyara.inventoryservice.domain.port.StockHistoryRepository;
import com.aliyara.inventoryservice.domain.port.StockRepository;
import com.aliyara.inventoryservice.domain.stock.Stock;
import com.aliyara.inventoryservice.domain.stock.StockHistory;
import com.aliyara.inventoryservice.domain.stock.enums.MovementType;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService implements StockUseCase {

    private final StockRepository stockRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final StockDtoMapper stockDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockByProductId(UUID productId) {
        Stock stock = findStockByProductId(productId);
        return stockDtoMapper.toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getAllStocks() {
        String tenantId = TenantContextHolder.getTenantId();
        return stockRepository.findAllByTenantId(tenantId)
                .stream()
                .map(stockDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockResponse adjustQuantity(UUID productId, AdjustStockRequest request) {
        Stock stock = findStockByProductId(productId);
        int quantityBefore = stock.getQuantityTotal();

        stock.adjustQuantity(request.getNewQuantityTotal());

        StockHistory history = new StockHistory.Builder()
                .productId(productId)
                .tenantId(resolveTenantId(stock))
                .movementType(MovementType.ADJUSTMENT)
                .quantityBefore(quantityBefore)
                .quantityAfter(stock.getQuantityTotal())
                .quantityChange(stock.getQuantityTotal() - quantityBefore)
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .performedBy(request.getPerformedBy())
                .build();

        stockHistoryRepository.save(history);
        Stock saved = stockRepository.save(stock);
        return stockDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public StockResponse reserveStock(UUID productId, ReserveStockRequest request) {
        Stock stock = findStockByProductId(productId);
        int quantityBefore = stock.getQuantityTotal();

        stock.reserve(request.getQuantity());

        StockHistory history = new StockHistory.Builder()
                .productId(productId)
                .tenantId(resolveTenantId(stock))
                .movementType(MovementType.OUT)
                .quantityBefore(quantityBefore)
                .quantityAfter(stock.getQuantityTotal())
                .quantityChange(-request.getQuantity())
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .performedBy(request.getPerformedBy())
                .build();

        stockHistoryRepository.save(history);
        Stock saved = stockRepository.save(stock);
        return stockDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public StockResponse releaseReservation(UUID productId, ReserveStockRequest request) {
        Stock stock = findStockByProductId(productId);
        int quantityBefore = stock.getQuantityTotal();

        stock.releaseReservation(request.getQuantity());

        StockHistory history = new StockHistory.Builder()
                .productId(productId)
                .tenantId(resolveTenantId(stock))
                .movementType(MovementType.RETURN)
                .quantityBefore(quantityBefore)
                .quantityAfter(stock.getQuantityTotal())
                .quantityChange(request.getQuantity())
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .performedBy(request.getPerformedBy())
                .build();

        stockHistoryRepository.save(history);
        Stock saved = stockRepository.save(stock);
        return stockDtoMapper.toResponse(saved);
    }

    private Stock findStockByProductId(UUID productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new StockNotFoundException("Stock not found for product: " + productId));
    }

    private String resolveTenantId(Stock stock) {
        String tenantId = TenantContextHolder.getTenantId();
        return tenantId != null ? tenantId : "unknown";
    }
}
