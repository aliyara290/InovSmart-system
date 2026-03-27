package com.aliyara.generatorservice.adapter.out.pdf;

import com.aliyara.generatorservice.application.port.out.PdfConverterPort;
import com.aliyara.generatorservice.domain.exception.DocumentGenerationException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Slf4j
@Component
public class OpenHtmlToPdfAdapter implements PdfConverterPort {

    @Override
    public byte[] convertHtmlToPdf(String html) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            log.error("Failed to convert HTML to PDF", e);
            throw new DocumentGenerationException("PDF conversion failed", e);
        }
    }
}
