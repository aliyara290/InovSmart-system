package com.aliyara.companyservice.infrastructure.mapper;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyUserJpaEntity;
import com.aliyara.companyservice.domain.model.CompanyUser;
import com.aliyara.companyservice.interfaces.dto.response.CompanyUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyUserMapper {
    CompanyUser toDomain(CompanyUserJpaEntity entity);
    CompanyUserJpaEntity toJpaEntity(CompanyUser domain);
    CompanyUserResponse toResponse(CompanyUser domain);
}
