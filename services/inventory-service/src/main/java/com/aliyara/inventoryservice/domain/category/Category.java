package com.aliyara.inventoryservice.domain.category;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Category {

    private final UUID id;
    private String name;
    private String description;

    private Category(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.name = builder.name;
        this.description = builder.description;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;

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
