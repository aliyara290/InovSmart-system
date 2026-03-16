package com.aliyara.companyservice.infrastructure.mapper;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyInvitationJpaEntity;
import com.aliyara.companyservice.domain.model.CompanyInvitation;
import com.aliyara.companyservice.interfaces.dto.response.CompanyInvitationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyInvitationMapper {
    CompanyInvitation toDomain(CompanyInvitationJpaEntity entity);
    CompanyInvitationJpaEntity toJpaEntity(CompanyInvitation domain);
    
//    @Mapping(target = "token", ignore = true)
    CompanyInvitationResponse toResponse(CompanyInvitation domain);
}
