package com.aliyara.billingservice.application.dto.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDocumentEvent {

    private String documentId;
    private String type;
    private CompanySnapshotDto company;
    private CustomerSnapshotDto customer;
    private List<LineItemDto> items;
    private TotalsDto totals;
}
