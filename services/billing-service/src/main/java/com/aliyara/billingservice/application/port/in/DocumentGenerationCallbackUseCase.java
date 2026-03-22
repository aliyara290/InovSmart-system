package com.aliyara.billingservice.application.port.in;

public interface DocumentGenerationCallbackUseCase {

    void onDocumentGenerated(String documentId, String type, String documentUrl);

    void onDocumentFailed(String documentId, String type);
}
