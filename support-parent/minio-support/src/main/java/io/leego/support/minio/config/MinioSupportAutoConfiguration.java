package io.leego.support.minio.config;

import io.leego.support.minio.manager.MinioManager;
import io.leego.support.minio.manager.impl.MinioManagerImpl;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

/**
 * @author Leego Yih
 */
@Configuration
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

    @EventListener(ContextRefreshedEvent.class)
    public void initMinioAfterStartup(ContextRefreshedEvent event) throws Exception {
        ApplicationContext applicationContext = event.getApplicationContext();
        MinioClient minioClient = applicationContext.getBean(MinioClient.class);
        MinioProperties properties = applicationContext.getBean(MinioProperties.class);
        ensureBuckets(minioClient, properties.getBuckets());
    }

    private synchronized void ensureBuckets(MinioClient minioClient, Map<String, String> buckets) throws Exception {
        if (buckets == null || buckets.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : buckets.entrySet()) {
            String bucket = entry.getValue();
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        }
    }
}
