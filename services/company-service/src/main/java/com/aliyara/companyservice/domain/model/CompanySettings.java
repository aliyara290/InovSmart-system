package com.aliyara.companyservice.domain.model;

public class CompanySettings {
    private final String id;
    private final String tenantId;
    private String logo;
    private int vat;
    private String currency;
    private String language;


    public CompanySettings(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.logo = builder.logo;
        this.vat = builder.vat;
        this.currency = builder.currency;
        this.language = builder.language;
    }

    public static class Builder{
        private String id;
        private String tenantId;
        private String logo;
        private int vat;
        private String currency;
        private String language;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder setLogo(String logo) {
            this.logo = logo;
            return this;
        }

        public Builder setVat(int vat) {
            this.vat = vat;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public CompanySettings build() {
            return new CompanySettings(this);
        }
    }
}
