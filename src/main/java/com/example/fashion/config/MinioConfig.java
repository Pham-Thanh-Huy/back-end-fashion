package com.example.fashion.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.accessKey}")
    private String accesKey;

    @Value("${minio.secretKey}")
    private String secretKey;


    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accesKey, secretKey)
                .build();
    }
}
