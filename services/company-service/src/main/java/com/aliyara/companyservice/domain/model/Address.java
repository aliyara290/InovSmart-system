package com.aliyara.companyservice.domain.model;

public class Address {
    private final String id;
    private final String tenantId;
    private String StreetLine1;
    private String StreetLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;

    private Address(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.StreetLine1 = builder.StreetLine1;
        this.StreetLine2 = builder.StreetLine2;
        this.city = builder.city;
        this.stateProvince = builder.stateProvince;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
    }

    public static class Builder {
        private String id;
        private String tenantId;
        private String StreetLine1;
        private String StreetLine2;
        private String city;
        private String stateProvince;
        private String postalCode;
        private String country;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder setStreetLine1(String streetLine1) {
            StreetLine1 = streetLine1;
            return this;
        }

        public Builder setStreetLine2(String streetLine2) {
            StreetLine2 = streetLine2;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setStateProvince(String stateProvince) {
            this.stateProvince = stateProvince;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
