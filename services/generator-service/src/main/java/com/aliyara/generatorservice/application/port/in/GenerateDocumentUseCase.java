package com.aliyara.generatorservice.application.port.in;

import com.aliyara.generatorservice.application.dto.GenerateDocumentCommand;

public interface GenerateDocumentUseCase {

    void generateDocument(GenerateDocumentCommand command);
}
