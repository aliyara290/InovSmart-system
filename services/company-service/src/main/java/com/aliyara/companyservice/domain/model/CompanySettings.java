package com.aliyara.companyservice.domain.model;

import lombok.Getter;
import java.util.Objects;

@Getter
public class CompanySettings {
    private String logo;
    private Integer vat;
    private String currency;
    private String language;

    public CompanySettings(Builder builder) {
        validate(builder);
        this.logo = builder.logo;
        this.vat = builder.vat;
        this.currency = builder.currency;
        this.language = builder.language;
    }

    public static class Builder {
        private String logo;
        private Integer vat;
        private String currency;
        private String language;

        public Builder setLogo(String logo) {
            this.logo = logo;
            return this;
        }

        public Builder setVat(Integer vat) {
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

    private void validate(Builder builder) {

        if (builder.vat == null || builder.vat <= 0) {
            throw new IllegalArgumentException("Invalid VAT!");
        }

        if (builder.currency == null || builder.currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }

        if (builder.language == null || builder.language.isBlank()) {
            throw new IllegalArgumentException("Language cannot be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        CompanySettings that = (CompanySettings) o;
        return Objects.equals(logo, that.logo) && Objects.equals(vat, that.vat)
                && Objects.equals(currency, that.currency) && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logo, vat, currency, language);
    }
}
