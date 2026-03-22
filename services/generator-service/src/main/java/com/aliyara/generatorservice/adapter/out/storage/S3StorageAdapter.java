package com.aliyara.generatorservice.adapter.out.storage;

import com.aliyara.generatorservice.application.port.out.StoragePort;
import com.aliyara.generatorservice.domain.exception.DocumentGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3StorageAdapter implements StoragePort {

    private final S3Client s3Client;

    @Value("${app.aws.s3.bucket}")
    private String bucketName;

    @Value("${app.aws.s3.region}")
    private String region;

    @Override
    public String upload(String key, byte[] content, String contentType) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(content));

            String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
            log.info("File uploaded to S3: {}", url);
            return url;
        } catch (Exception e) {
            log.error("Failed to upload file to S3, key: {}", key, e);
            throw new DocumentGenerationException("S3 upload failed for key: " + key, e);
        }
    }
}
