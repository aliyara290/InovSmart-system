package com.aliyara.companyservice.infrastrecture.adapter.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingsEmbeddable {
    private String logo;
    private Integer vat;
    private String currency;
    private String language;
}
