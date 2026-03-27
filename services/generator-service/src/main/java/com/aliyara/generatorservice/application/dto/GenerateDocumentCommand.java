package com.aliyara.generatorservice.application.dto;

import com.aliyara.generatorservice.domain.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDocumentCommand {

    private String documentId;
    private DocumentType type;
    private CompanyData company;
    private CustomerData customer;
    private List<LineItemData> items;
    private TotalsData totals;
}
