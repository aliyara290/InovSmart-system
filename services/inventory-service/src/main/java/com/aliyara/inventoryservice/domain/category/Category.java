package com.aliyara.inventoryservice.domain.category;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Category {

    private final UUID id;
    private String name;
    private String description;
    private final LocalDateTime createdAt;

    private Category(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.name = builder.name;
        this.description = builder.description;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    private void validate(Builder builder) {
        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
    }
}
