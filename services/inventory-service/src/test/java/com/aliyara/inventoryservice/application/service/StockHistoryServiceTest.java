package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.stock.StockHistoryResponse;
import com.aliyara.inventoryservice.application.mapper.StockDtoMapper;
import com.aliyara.inventoryservice.domain.model.stock.StockHistory;
import com.aliyara.inventoryservice.domain.model.stock.enums.MovementType;
import com.aliyara.inventoryservice.domain.model.stock.enums.ReferenceType;
import com.aliyara.inventoryservice.domain.port.StockHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockHistoryServiceTest {

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @Mock
    private StockDtoMapper stockDtoMapper;

    @InjectMocks
    private StockHistoryService stockHistoryService;

    private UUID productId;
    private StockHistory stockHistory;
    private StockHistoryResponse historyResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        
        stockHistory = new StockHistory.Builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .tenantId("tenant-123")
                .movementType(MovementType.IN)
                .quantityBefore(100)
                .quantityAfter(150)
                .quantityChange(50)
                .reason("Stock replenishment")
                .referenceType(ReferenceType.PURCHASE)
                .referenceId(UUID.randomUUID())
                .performedBy(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
                .build();
                
        historyResponse = new StockHistoryResponse();
        historyResponse.setId(stockHistory.getId());
        historyResponse.setProductId(productId);
        historyResponse.setMovementType(MovementType.IN);
        historyResponse.setQuantityBefore(100);
        historyResponse.setQuantityAfter(150);
        historyResponse.setQuantityChange(50);
        historyResponse.setReason("Stock replenishment");
    }

    @Test
    void getHistoryByProductId_shouldReturnHistory_whenExists() {
        List<StockHistory> histories = List.of(stockHistory);
        when(stockHistoryRepository.findByProductId(productId)).thenReturn(histories);
        when(stockDtoMapper.toHistoryResponse(stockHistory)).thenReturn(historyResponse);

        List<StockHistoryResponse> results = stockHistoryService.getHistoryByProductId(productId);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(productId, results.get(0).getProductId());
        assertEquals(MovementType.IN, results.get(0).getMovementType());
        verify(stockHistoryRepository).findByProductId(productId);
    }

    @Test
    void getHistoryByProductId_shouldReturnEmptyList_whenNoHistory() {
        when(stockHistoryRepository.findByProductId(productId)).thenReturn(List.of());

        List<StockHistoryResponse> results = stockHistoryService.getHistoryByProductId(productId);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(stockHistoryRepository).findByProductId(productId);
    }

    @Test
    void getHistoryByProductId_shouldMapAllHistories() {
        StockHistory history2 = new StockHistory.Builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .tenantId("tenant-123")
                .movementType(MovementType.OUT)
                .quantityBefore(150)
                .quantityAfter(130)
                .quantityChange(-20)
                .reason("Sale")
                .referenceType(ReferenceType.ORDER)
                .referenceId(UUID.randomUUID())
                .performedBy(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
                .build();

        StockHistoryResponse response2 = new StockHistoryResponse();
        response2.setId(history2.getId());
        response2.setProductId(productId);
        response2.setMovementType(MovementType.OUT);

        List<StockHistory> histories = List.of(stockHistory, history2);
        when(stockHistoryRepository.findByProductId(productId)).thenReturn(histories);
        when(stockDtoMapper.toHistoryResponse(stockHistory)).thenReturn(historyResponse);
        when(stockDtoMapper.toHistoryResponse(history2)).thenReturn(response2);

        List<StockHistoryResponse> results = stockHistoryService.getHistoryByProductId(productId);

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(stockDtoMapper, times(2)).toHistoryResponse(any(StockHistory.class));
    }
}
