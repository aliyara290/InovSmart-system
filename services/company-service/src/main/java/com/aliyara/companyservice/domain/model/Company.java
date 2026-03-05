package com.aliyara.companyservice.domain.model;

import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Company {

    private final CompanyId id;
    private final TenantId tenantId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private int size;
    private int foundedYear;
    private String description;
    private Address address;
    private CompanySettings companySettings;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Company(Builder builder) {
        this.id = builder.id != null ? builder.id : new CompanyId();
        this.tenantId = builder.tenantId != null ? builder.tenantId : new TenantId();
        validate(builder);
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.website = builder.website;
        this.size = builder.size;
        this.foundedYear = builder.foundedYear;
        this.description = builder.description;
        this.address = builder.address;
        this.companySettings = builder.companySettings;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    public static class Builder {
        private CompanyId id;
        private TenantId tenantId;
        private String name;
        private String email;
        private String phone;
        private String website;
        private int size;
        private int foundedYear;
        private String description;
        private Address address;
        private CompanySettings companySettings;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(CompanyId id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(TenantId tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder foundedYear(int foundedYear) {
            this.foundedYear = foundedYear;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder companySettings(CompanySettings companySettings) {
            this.companySettings = companySettings;
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

        public Company build() {
            return new Company(this);
        }
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContactInfo(String phone, String website) {
        this.phone = phone;
        this.website = website;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCompanySettings(CompanySettings companySettings) {
        if (companySettings == null) {
            throw new IllegalArgumentException("Company settings cannot be null");
        }
        this.companySettings = companySettings;
        this.updatedAt = LocalDateTime.now();
    }

    private void validate(Builder builder) {
        if (this.tenantId == null || this.tenantId.id() == null) {
            throw new IllegalArgumentException("Company tenantId cannot be empty");
        }

        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }

        if (builder.email == null || !builder.email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (builder.phone == null || builder.phone.isBlank()) {
            throw new IllegalArgumentException("Company phone cannot be empty");
        }
    }

}