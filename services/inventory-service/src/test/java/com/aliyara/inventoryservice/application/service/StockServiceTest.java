package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.stock.AdjustStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.ReserveStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;
import com.aliyara.inventoryservice.application.mapper.StockDtoMapper;
import com.aliyara.inventoryservice.domain.exception.StockNotFoundException;
import com.aliyara.inventoryservice.domain.model.stock.Stock;
import com.aliyara.inventoryservice.domain.model.stock.StockHistory;
import com.aliyara.inventoryservice.domain.model.stock.enums.ReferenceType;
import com.aliyara.inventoryservice.domain.model.stock.enums.StockStatus;
import com.aliyara.inventoryservice.domain.port.StockHistoryRepository;
import com.aliyara.inventoryservice.domain.port.StockRepository;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @Mock
    private StockDtoMapper stockDtoMapper;

    @InjectMocks
    private StockService stockService;

    private static final String TENANT_ID = "tenant-123";
    private UUID productId;
    private Stock stock;
    private StockResponse stockResponse;

    @BeforeEach
    void setUp() {
        TenantContextHolder.setTenantId(TENANT_ID);
        productId = UUID.randomUUID();
        
        stock = new Stock.Builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .quantityTotal(100)
                .quantityReserved(0)
                .minStock(10)
                .maxStock(200)
                .status(StockStatus.IN_STOCK)
                .build();
                
        stockResponse = new StockResponse();
        stockResponse.setProductId(productId);
        stockResponse.setQuantityTotal(100);
        stockResponse.setQuantityReserved(0);
        stockResponse.setStatus(StockStatus.IN_STOCK);
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    void getStockByProductId_shouldReturnStock_whenExists() {
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));
        when(stockDtoMapper.toResponse(stock)).thenReturn(stockResponse);

        StockResponse result = stockService.getStockByProductId(productId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        verify(stockRepository).findByProductId(productId);
    }

    @Test
    void getStockByProductId_shouldThrowException_whenNotFound() {
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, 
                () -> stockService.getStockByProductId(productId));
    }

    @Test
    void getAllStocks_shouldReturnAllStocks() {
        List<Stock> stocks = List.of(stock);
        when(stockRepository.findAllByTenantId(TENANT_ID)).thenReturn(stocks);
        when(stockDtoMapper.toResponse(stock)).thenReturn(stockResponse);

        List<StockResponse> results = stockService.getAllStocks();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(stockRepository).findAllByTenantId(TENANT_ID);
    }

    @Test
    void adjustQuantity_shouldAdjustStockAndSaveHistory() {
        AdjustStockRequest request = new AdjustStockRequest();
        request.setNewQuantityTotal(150);
        request.setReason("Inventory adjustment");
        request.setReferenceType(ReferenceType.MANUAL);
        request.setReferenceId(UUID.randomUUID());
        request.setPerformedBy(UUID.randomUUID());

        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockHistoryRepository.save(any(StockHistory.class))).thenReturn(any());
        when(stockDtoMapper.toResponse(stock)).thenReturn(stockResponse);

        StockResponse result = stockService.adjustQuantity(productId, request);

        assertNotNull(result);
        verify(stockRepository).save(any(Stock.class));
        verify(stockHistoryRepository).save(any(StockHistory.class));
    }

    @Test
    void adjustQuantity_shouldThrowException_whenStockNotFound() {
        AdjustStockRequest request = new AdjustStockRequest();
        request.setNewQuantityTotal(150);

        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, 
                () -> stockService.adjustQuantity(productId, request));
    }

    @Test
    void reserveStock_shouldReserveStockAndSaveHistory() {
        ReserveStockRequest request = new ReserveStockRequest();
        request.setQuantity(20);
        request.setReason("Order placement");
        request.setReferenceType(ReferenceType.ORDER);
        request.setReferenceId(UUID.randomUUID());
        request.setPerformedBy(UUID.randomUUID());

        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockHistoryRepository.save(any(StockHistory.class))).thenReturn(any());
        when(stockDtoMapper.toResponse(stock)).thenReturn(stockResponse);

        StockResponse result = stockService.reserveStock(productId, request);

        assertNotNull(result);
        verify(stockRepository).save(any(Stock.class));
        verify(stockHistoryRepository).save(any(StockHistory.class));
    }

    @Test
    void reserveStock_shouldThrowException_whenStockNotFound() {
        ReserveStockRequest request = new ReserveStockRequest();
        request.setQuantity(20);

        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, 
                () -> stockService.reserveStock(productId, request));
    }

    @Test
    void releaseReservation_shouldThrowException_whenStockNotFound() {
        ReserveStockRequest request = new ReserveStockRequest();
        request.setQuantity(10);

        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, 
                () -> stockService.releaseReservation(productId, request));
    }
}
