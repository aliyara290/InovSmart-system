package com.aliyara.companyservice.infrastrecture.adapter.persistence.mapper;

import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.CompanyEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CompanyPersistenceMapper {

    CompanyEntity toEntity(Company company);

    default Company toDomain(CompanyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Company.Builder()
                .id(entity.getId() == null ? null : new CompanyId(entity.getId()))
                .tenantId(entity.getTenantId() == null ? null : new TenantId(entity.getTenantId()))
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .website(entity.getWebsite())
                .size(entity.getSize())
                .foundedYear(entity.getFoundedYear())
                .description(entity.getDescription())
                .address(toAddress(entity.getAddress()))
                .companySettings(toCompanySettings(entity.getCompanySettings()))
                .build();
    }

    default com.aliyara.companyservice.domain.model.Address toAddress(
            com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.AddressEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new com.aliyara.companyservice.domain.model.Address.Builder()
                .setStreetLine1(embeddable.getStreetLine1())
                .setStreetLine2(embeddable.getStreetLine2())
                .setCity(embeddable.getCity())
                .setStateProvince(embeddable.getStateProvince())
                .setPostalCode(embeddable.getPostalCode())
                .setCountry(embeddable.getCountry())
                .build();
    }

    default com.aliyara.companyservice.domain.model.CompanySettings toCompanySettings(
            com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.CompanySettingsEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new com.aliyara.companyservice.domain.model.CompanySettings.Builder()
                .setLogo(embeddable.getLogo())
                .setVat(embeddable.getVat())
                .setCurrency(embeddable.getCurrency())
                .setLanguage(embeddable.getLanguage())
                .build();
    }

    com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.AddressEmbeddable toAddressEmbeddable(
            com.aliyara.companyservice.domain.model.Address address);

    com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.CompanySettingsEmbeddable toCompanySettingsEmbeddable(
            com.aliyara.companyservice.domain.model.CompanySettings companySettings);

    default UUID map(CompanyId id) {
        return id == null ? null : id.id();
    }

    default CompanyId map(UUID id) {
        return id == null ? null : new CompanyId(id);
    }

    default UUID map(TenantId id) {
        return id == null ? null : id.id();
    }

}
