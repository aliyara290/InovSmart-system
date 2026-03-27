package com.aliyara.generatorservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenerationResult {

    private final String documentId;
    private final DocumentType type;
    private final String documentUrl;
}
