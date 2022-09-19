package io.leego.support.minio.manager.impl;

import io.leego.support.minio.BucketKey;
import io.leego.support.minio.DirectoryKey;
import io.leego.support.minio.exception.MinioRemoveException;
import io.leego.support.minio.manager.MinioManager;
import io.minio.BucketExistsArgs;
import io.minio.GetBucketPolicyArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;

import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
public class MinioManagerImpl implements MinioManager {
    public static final int MIN_MULTIPART_SIZE = 10 * 1024 * 1024;
    private final MinioClient minioClient;
    private final Map<String, String> buckets;
    private final Map<String, String> directories;

    public MinioManagerImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
        this.buckets = Collections.emptyMap();
        this.directories = Collections.emptyMap();
    }

    public MinioManagerImpl(MinioClient minioClient, Map<String, String> buckets, Map<String, String> directories) {
        this.minioClient = minioClient;
        this.buckets = buckets;
        this.directories = directories;
    }

    @Override
    public String getBucketName(String key) {
        return buckets.get(key);
    }

    @Override
    public String getBucketName(BucketKey key) {
        return buckets.get(key.getKey());
    }

    @Override
    public String getDirectory(String key) {
        return directories.get(key);
    }

    @Override
    public String getDirectory(DirectoryKey key) {
        return directories.get(key.getKey());
    }

    @Override
    public void createBucket(String bucket) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }

    @Override
    public void removeBucket(String bucket) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }

    @Override
    public boolean containBucket(String bucket) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    @Override
    public void ensureBucket(String bucket) throws Exception {
        if (!containBucket(bucket)) {
            createBucket(bucket);
        }
    }

    @Override
    public void ensureAllBuckets() throws Exception {
        for (Map.Entry<String, String> entry : buckets.entrySet()) {
            String bucket = entry.getValue();
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        }
    }

    @Override
    public String getBucketPolicy(String bucket) throws Exception {
        return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucket).build());
    }

    @Override
    public void put(String bucket, String key, InputStream inputStream) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(key).stream(inputStream, -1, MIN_MULTIPART_SIZE).build());
    }

    @Override
    public InputStream get(String bucket, String key) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(key).build());
    }

    @Override
    public void remove(String bucket, String key) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());
    }

    @Override
    public void removeMulti(String bucket, String... keys) throws Exception {
        if (keys == null || keys.length == 0) {
            return;
        }
        removeMulti(bucket, Arrays.stream(keys).map(DeleteObject::new).collect(Collectors.toList()));
    }

    @Override
    public void removeMulti(String bucket, List<String> keys) throws Exception {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        removeMulti(bucket, keys.stream().map(DeleteObject::new).collect(Collectors.toList()));
    }

    private void removeMulti(String bucket, Iterable<DeleteObject> objects) throws Exception {
        Iterator<Result<DeleteError>> iterator = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(objects).build()).iterator();
        if (!iterator.hasNext()) {
            return;
        }
        StringBuilder sb = new StringBuilder().append("Failed to delete objects, the bucket name is \"").append(bucket).append("\".");
        while (iterator.hasNext()) {
            DeleteError error = iterator.next().get();
            sb.append("\nname=").append(error.objectName()).append(", code=").append(error.code()).append(", Message=").append(error.message());
        }
        throw new MinioRemoveException(sb.toString());
    }

    @Override
    public String getUrl(String bucket, String key) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .method(Method.GET)
                        .build());
    }

    @Override
    public String getUrl(String bucket, String key, Duration duration) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .expiry((int) Math.min(duration.toSeconds(), GetPresignedObjectUrlArgs.DEFAULT_EXPIRY_TIME))
                        .method(Method.GET)
                        .build());
    }

}
