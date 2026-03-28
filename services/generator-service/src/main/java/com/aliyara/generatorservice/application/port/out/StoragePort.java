package com.aliyara.generatorservice.application.port.out;

public interface StoragePort {

    String upload(String key, byte[] content, String contentType);
}
