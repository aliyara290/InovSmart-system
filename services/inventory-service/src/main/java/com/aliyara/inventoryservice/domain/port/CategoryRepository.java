package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(UUID id);

    List<Category> findAll();

    void deleteById(UUID id);
}
