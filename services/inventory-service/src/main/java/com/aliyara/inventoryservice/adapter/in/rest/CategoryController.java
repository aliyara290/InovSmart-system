package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.application.port.in.CategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryUseCase.createCategory(request);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable UUID id) {
        return categoryUseCase.getCategory(id);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryUseCase.getAllCategories();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryUseCase.deleteCategory(id);
    }
}
