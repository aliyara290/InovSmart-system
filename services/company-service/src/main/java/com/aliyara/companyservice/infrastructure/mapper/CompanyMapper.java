package com.aliyara.companyservice.infrastructure.mapper;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyJpaEntity;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.interfaces.dto.request.RegisterCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.response.CompanyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company toDomain(CompanyJpaEntity entity);
    CompanyJpaEntity toJpaEntity(Company domain);
    CompanyResponse toResponse(Company domain);
    Company toDomain(RegisterCompanyRequest.CompanyDto dto);
}
