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
public class AddressEmbeddable {
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
}
