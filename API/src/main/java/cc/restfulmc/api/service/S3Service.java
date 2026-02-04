package cc.restfulmc.api.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Braydon
 */
@Service @Log4j2(topic = "S3")
public final class S3Service {
    @NonNull private final MinioClient minioClient;
    @NonNull private final Cache<ObjectCacheKey, byte[]> objectCache;

    @Autowired @SneakyThrows
    public S3Service(@Value("${storage.s3.endpoint}") String endpoint,
                     @Value("${storage.s3.accessKey}") String accessKey,
                     @Value("${storage.s3.secretKey}") String secretKey,
                     @Value("${storage.s3.ttl}") int objectCacheTtl
    ) {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        objectCache = CacheBuilder.newBuilder()
                .expireAfterWrite(objectCacheTtl, TimeUnit.MINUTES)
                .build();

        // Create all buckets if they don't exist
        for (Bucket bucket : Bucket.values()) {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket.getName()).build())) {
                continue;
            }
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket.getName())
                    .build());
        }
    }

    /**
     * Uploads a file to the given bucket.
     *
     * @param bucket the bucket to upload to
     * @param fileName the name of the file
     * @param data the data to upload
     */
    @SneakyThrows
    public void upload(@NonNull Bucket bucket, @NonNull String fileName, @NonNull String contentType, byte[] data) {
        try {
            long before = System.currentTimeMillis();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket.getName())
                    .object(fileName)
                    .stream(new ByteArrayInputStream(data), data.length, -1)
                    .contentType(contentType)
                    .build());
            objectCache.put(new ObjectCacheKey(bucket, fileName), data);
            log.info("Uploaded object {} to bucket {} in {}ms", fileName, bucket.getName(), System.currentTimeMillis() - before);
        } catch (Exception ex) {
            log.error("Failed to upload file to bucket {}", bucket.getName(), ex);
        }
    }

    /**
     * Gets a file from the given bucket.
     *
     * @param bucket the bucket to get from
     * @param fileName the name of the file
     * @return the file data
     */
    @SneakyThrows
    public byte[] get(@NonNull Bucket bucket, @NonNull String fileName) {
        try {
            byte[] object = objectCache.getIfPresent(new ObjectCacheKey(bucket, fileName));
            if (object != null) {
                return object;
            }
            long before = System.currentTimeMillis();
            byte[] bytes = minioClient.getObject(GetObjectArgs.builder()
                            .bucket(bucket.getName())
                            .object(fileName)
                            .build()).readAllBytes();
            objectCache.put(new ObjectCacheKey(bucket, fileName), bytes);
            log.info("Got object {} from bucket {} in {}ms", fileName, bucket.getName(), System.currentTimeMillis() - before);
            return bytes;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Cache key for the in-memory object cache.
     *
     * @param bucket the bucket it is stored in
     * @param fileName the name of the file
     */
    public record ObjectCacheKey(@NonNull Bucket bucket, @NonNull String fileName) {}

    @AllArgsConstructor @Getter
    public enum Bucket {
        SKINS("restfulmc-skins");

        @NonNull private final String name;
    }
}