package com.aliyara.inventoryservice.application.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponse {
    private UUID id;
    private String name;
}
