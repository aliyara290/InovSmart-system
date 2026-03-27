package com.aliyara.generatorservice.application.port.out;

public interface PdfConverterPort {

    byte[] convertHtmlToPdf(String html);
}
