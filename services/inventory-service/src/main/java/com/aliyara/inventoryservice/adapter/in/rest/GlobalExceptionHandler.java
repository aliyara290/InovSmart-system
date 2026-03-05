package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.domain.exception.CategoryNotFoundException;
import com.aliyara.inventoryservice.domain.exception.DuplicateSkuException;
import com.aliyara.inventoryservice.domain.exception.InsufficientStockException;
import com.aliyara.inventoryservice.domain.exception.ProductNotFoundException;
import com.aliyara.inventoryservice.domain.exception.StockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object> handleStockNotFound(StockNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ResponseEntity<Object> handleDuplicateSku(DuplicateSkuException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String field = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            validationErrors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
    }

    private Map<String, String> error(String message) {
        return Map.of("error", message);
    }
}
