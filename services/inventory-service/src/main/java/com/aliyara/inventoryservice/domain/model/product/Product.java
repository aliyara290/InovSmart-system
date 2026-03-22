package com.aliyara.inventoryservice.domain.model.product;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Product {

    private final UUID id;
    private final String tenantId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private UUID productId;
    private UUID categoryId;
    private boolean active;

    private Product(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.sku = builder.sku;
        this.productId = builder.productId;
        this.categoryId = builder.categoryId;
        this.active = builder.active;
    }

    public static class Builder {
        private UUID id;
        private String tenantId;
        private String name;
        private String description;
        private double price;
        private String sku;
        private UUID productId;
        private UUID categoryId;
        private boolean active = true;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
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

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public void updateDetails(String name, String description, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void updateSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("Product SKU cannot be empty");
        }
        this.sku = sku;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("Product tenantId cannot be empty");
        }
        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (builder.sku == null || builder.sku.isBlank()) {
            throw new IllegalArgumentException("Product SKU cannot be empty");
        }
        if (builder.price < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }
}
