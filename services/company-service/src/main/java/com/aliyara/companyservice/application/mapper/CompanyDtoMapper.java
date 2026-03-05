package com.aliyara.companyservice.application.mapper;

import com.aliyara.companyservice.application.dto.CompanyRequest;
import com.aliyara.companyservice.application.dto.CompanyResponse;
import com.aliyara.companyservice.application.dto.CompanyUserResponse;
import com.aliyara.companyservice.domain.model.Address;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.CompanySettings;
import com.aliyara.companyservice.domain.model.User;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CompanyDtoMapper {

    default Company toDomain(CompanyRequest request) {
        if (request == null) {
            return null;
        }
        return new Company.Builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .size(request.getSize())
                .foundedYear(request.getFoundedYear())
                .description(request.getDescription())
                .address(toAddress(request.getAddress()))
                .companySettings(toCompanySettings(request.getCompanySettings()))
                .build();
    }

    default Address toAddress(CompanyRequest.AddressDto dto) {
        if (dto == null) {
            return null;
        }
        return new Address.Builder()
                .setStreetLine1(dto.getStreetLine1())
                .setStreetLine2(dto.getStreetLine2())
                .setCity(dto.getCity())
                .setStateProvince(dto.getStateProvince())
                .setPostalCode(dto.getPostalCode())
                .setCountry(dto.getCountry())
                .build();
    }

    default CompanySettings toCompanySettings(CompanyRequest.CompanySettingsDto dto) {
        if (dto == null) {
            return null;
        }
        return new CompanySettings.Builder()
                .setLogo(dto.getLogo())
                .setVat(dto.getVat())
                .setCurrency(dto.getCurrency())
                .setLanguage(dto.getLanguage())
                .build();
    }

    CompanyResponse toResponse(Company company);

    CompanyRequest.AddressDto toAddressDto(Address address);

    CompanyRequest.CompanySettingsDto toCompanySettingsDto(CompanySettings companySettings);

    CompanyUserResponse toUserResponse(User user);

    default UUID map(CompanyId value) {
        return value != null ? value.id() : null;
    }

    default UUID map(TenantId value) {
        return value != null ? value.id() : null;
    }
}
