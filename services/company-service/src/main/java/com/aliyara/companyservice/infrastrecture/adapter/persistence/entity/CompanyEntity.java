package com.aliyara.companyservice.infrastrecture.adapter.persistence.entity;

import com.aliyara.companyservice.domain.model.ids.CompanyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", unique = true, nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String website;

    @Column(name = "company_size")
    private int size;

    private int foundedYear;

    @Column(length = 1000)
    private String description;

    @Embedded
    private AddressEmbeddable address;

    @Embedded
    private CompanySettingsEmbeddable companySettings;
}
