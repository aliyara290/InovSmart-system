package com.aliyara.generatorservice.application.dto;

import com.aliyara.generatorservice.domain.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentGeneratedEvent {

    private String documentId;
    private DocumentType type;
    private String documentUrl;
}
