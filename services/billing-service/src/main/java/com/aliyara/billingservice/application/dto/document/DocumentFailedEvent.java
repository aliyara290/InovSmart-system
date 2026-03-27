package com.aliyara.billingservice.application.dto.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFailedEvent {

    private String documentId;
    private String type;
    private String reason;
}
