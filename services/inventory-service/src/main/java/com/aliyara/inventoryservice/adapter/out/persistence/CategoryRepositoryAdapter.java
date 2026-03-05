package com.aliyara.inventoryservice.adapter.out.persistence;

import com.aliyara.inventoryservice.adapter.out.persistence.jpa.CategoryJpaRepository;
import com.aliyara.inventoryservice.adapter.out.persistence.mapper.CategoryPersistenceMapper;
import com.aliyara.inventoryservice.domain.category.Category;
import com.aliyara.inventoryservice.domain.port.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    @Override
    public Category save(Category category) {
        var entity = categoryPersistenceMapper.toEntity(category);
        var saved = categoryJpaRepository.save(entity);
        return categoryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id)
                .map(categoryPersistenceMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        categoryJpaRepository.deleteById(id);
    }
}
