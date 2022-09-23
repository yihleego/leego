package io.leego.support.minio.config;

import io.leego.support.minio.manager.MinioManager;
import io.leego.support.minio.manager.impl.MinioManagerImpl;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Leego Yih
 */
@AutoConfiguration
@ConditionalOnProperty(value = "minio.enabled", matchIfMissing = true)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioSupportAutoConfiguration {

    @Bean
    @ConditionalOnMissingClass
    public MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    @Bean
    public MinioManager minioManager(MinioClient minioClient, MinioProperties properties) {
        return new MinioManagerImpl(minioClient, properties.getBuckets(), properties.getDirectories());
    }

}
