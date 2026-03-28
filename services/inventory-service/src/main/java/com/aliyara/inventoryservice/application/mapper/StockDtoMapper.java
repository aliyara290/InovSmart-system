package com.aliyara.inventoryservice.application.mapper;

import com.aliyara.inventoryservice.application.dto.stock.StockHistoryResponse;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;
import com.aliyara.inventoryservice.domain.model.stock.Stock;
import com.aliyara.inventoryservice.domain.model.stock.StockHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockDtoMapper {

    @Mapping(target = "availableQuantity", expression = "java(stock.getAvailableQuantity())")
    StockResponse toResponse(Stock stock);

    StockHistoryResponse toHistoryResponse(StockHistory stockHistory);
}
