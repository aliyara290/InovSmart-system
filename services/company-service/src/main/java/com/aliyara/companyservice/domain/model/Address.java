package com.aliyara.companyservice.domain.model;

import lombok.Getter;
import java.util.Objects;

@Getter
public class Address {
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;

    private Address(Builder builder) {
        validate(builder);
        this.streetLine1 = builder.streetLine1;
        this.streetLine2 = builder.streetLine2;
        this.city = builder.city;
        this.stateProvince = builder.stateProvince;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
    }

    public static class Builder {
        private String streetLine1;
        private String streetLine2;
        private String city;
        private String stateProvince;
        private String postalCode;
        private String country;

        public Builder setStreetLine1(String streetLine1) {
            this.streetLine1 = streetLine1;
            return this;
        }

        public Builder setStreetLine2(String streetLine2) {
            this.streetLine2 = streetLine2;
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

    private void validate(Builder builder) {
        if (builder.streetLine1 == null || builder.streetLine1.isBlank()) {
            throw new IllegalArgumentException("Street line 1 cannot be empty");
        }

        if (builder.city == null || builder.city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }

        if (builder.country == null || builder.country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(streetLine1, address.streetLine1) &&
                Objects.equals(streetLine2, address.streetLine2) &&
                Objects.equals(city, address.city) &&
                Objects.equals(stateProvince, address.stateProvince) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetLine1, streetLine2, city, stateProvince, postalCode, country);
    }
}
