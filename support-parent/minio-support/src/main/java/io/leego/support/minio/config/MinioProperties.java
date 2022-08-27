package io.leego.support.minio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

/**
 * @author Leego Yih
 */
@ConfigurationProperties("minio")
public class MinioProperties {
    private boolean enabled = true;
    private String endpoint = "http://localhost:9000";
    private String accessKey;
    private String secretKey;
    private Map<String, String> buckets = Collections.emptyMap();
    private Map<String, String> directories = Collections.emptyMap();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Map<String, String> getBuckets() {
        return buckets;
    }

    public void setBuckets(Map<String, String> buckets) {
        this.buckets = buckets;
    }

    public Map<String, String> getDirectories() {
        return directories;
    }

    public void setDirectories(Map<String, String> directories) {
        this.directories = directories;
    }
}
