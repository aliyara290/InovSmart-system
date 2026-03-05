package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
}
