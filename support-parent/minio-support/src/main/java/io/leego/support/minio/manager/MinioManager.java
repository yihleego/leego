package io.leego.support.minio.manager;

import io.leego.support.minio.BucketKey;
import io.leego.support.minio.DirectoryKey;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;

/**
 * @author Leego Yih
 */
public interface MinioManager {

    String getBucketName(String key);

    String getBucketName(BucketKey key);

    String getDirectory(String key);

    String getDirectory(DirectoryKey key);

    void createBucket(String bucket) throws Exception;

    void removeBucket(String bucket) throws Exception;

    boolean containBucket(String bucket) throws Exception;

    void ensureBucket(String bucket) throws Exception;

    void ensureAllBuckets() throws Exception;

    String getBucketPolicy(String bucket) throws Exception;

    void put(String bucket, String key, InputStream inputStream) throws Exception;

    InputStream get(String bucket, String key) throws Exception;

    void remove(String bucket, String key) throws Exception;

    void removeMulti(String bucket, String... keys) throws Exception;

    void removeMulti(String bucket, List<String> keys) throws Exception;

    String getUrl(String bucket, String key) throws Exception;

    String getUrl(String bucket, String key, Duration duration) throws Exception;

}
