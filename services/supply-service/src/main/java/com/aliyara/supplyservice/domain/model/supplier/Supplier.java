package com.aliyara.supplyservice.domain.model.supplier;

import com.aliyara.supplyservice.domain.model.supplier.enums.SupplierStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Supplier {

    private final UUID id;
    private final String tenantId;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;
    private SupplierStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Supplier(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.name = builder.name;
        this.contactEmail = builder.contactEmail;
        this.phone = builder.phone;
        this.address = builder.address;
        this.status = builder.status != null ? builder.status : SupplierStatus.ACTIVE;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    public void updateStatus(SupplierStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Supplier status cannot be null");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDetails(String name, String contactEmail, String phone, String address) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("Supplier tenantId cannot be empty");
        }
        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }
    }

    public static class Builder {
        private UUID id;
        private String tenantId;
        private String name;
        private String contactEmail;
        private String phone;
        private String address;
        private SupplierStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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

        public Builder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder status(SupplierStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Supplier build() {
            return new Supplier(this);
        }
    }
}

