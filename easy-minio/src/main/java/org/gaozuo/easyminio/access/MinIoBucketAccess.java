package org.gaozuo.easyminio.access;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import lombok.NonNull;
import org.gaozuo.easyio.access.ResourceBucket;
import org.gaozuo.easyio.access.StandaloneBucket;
import org.gaozuo.easyio.access.PathAccess;
import org.gaozuo.easyminio.MinIoUtils;
import org.gaozuo.easyminio.operation.MinIoLocation;
import org.gaozuo.easyminio.operation.MinIoObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;

public class MinIoBucketAccess implements PathAccess {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @NonNull private final MinioClient client;
    @NonNull private final String bucket;
    private final String prefix;

    public MinIoBucketAccess(@NonNull MinioClient client, @NonNull String bucket, String prefix) {
        if (!MinIoUtils.checkBucket(bucket)) {
            throw new IllegalArgumentException("Not a valid bucket: " + bucket);
        }
        this.client = client;
        this.bucket = bucket;
        this.prefix = prefix;
    }

    @Override
    public boolean canUse() throws Exception {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    @Override
    public void construct() throws Exception {
        if (!canUse()) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    @Override
    public ResourceBucket bucket() {
        ListObjectsArgs.Builder builder = ListObjectsArgs
            .builder()
              .bucket(bucket);
        if (prefix != null && !prefix.isEmpty()) {
            builder.prefix(prefix);
        }
        StandaloneBucket standaloneBucket = new StandaloneBucket();
        for (Result<io.minio.messages.Item> result : client.listObjects(builder.build())) {
            try {
                io.minio.messages.Item item = result.get();
                if (!item.isDir()) {
                    MinIoObject object = MinIoObject.create(bucket, item.objectName(), client);
                    standaloneBucket.addToObjectResources(object);
                } else {
                    MinIoLocation location = MinIoLocation.fromBucketAndPrefix(bucket, item.objectName(), client);
                    standaloneBucket.addToPathResources(location);
                }
            } catch (ErrorResponseException |
                IllegalArgumentException |
                InsufficientDataException |
                InternalException |
                InvalidKeyException |
                InvalidResponseException |
                IOException |
                NoSuchAlgorithmException |
                ServerException |
                XmlParserException e) {
                logger.debug("Found error item.", e);
                standaloneBucket.addToErrors(e);
            }
        }
        return standaloneBucket;
    }
}
