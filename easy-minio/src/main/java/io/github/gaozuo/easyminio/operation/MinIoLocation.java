package io.github.gaozuo.easyminio.operation;

import io.github.gaozuo.easyminio.MinIoUtils;
import io.github.gaozuo.easyminio.access.MinIoBucketAccess;
import io.minio.MinioClient;
import io.github.gaozuo.easyio.access.PathAccess;
import io.github.gaozuo.easyio.source.PathResource;

public class MinIoLocation implements PathResource {

    private final String bucket;
    private final MinioClient client;
    private final String prefix;

    private MinIoLocation(String prefix, String bucket, MinioClient client) {
        if (!MinIoUtils.checkBucket(bucket)) {
            throw new IllegalArgumentException("Not a valid bucket: " + bucket);
        }
        this.prefix = prefix;
        this.bucket = bucket;
        this.client = client;
    }

    public static MinIoLocation fromBucket(String bucket, MinioClient client) {
        return new MinIoLocation(null, bucket, client);
    }

    public static MinIoLocation fromBucketAndPrefix(String bucket, String prefix, MinioClient client) {
        return new MinIoLocation(prefix, bucket, client);
    }

    @Override
    public String path() {
        return MinIoUtils.checkPath(prefix) ? MinIoUtils.concatToken(bucket, prefix) : bucket;
    }

    @Override
    public PathAccess access() {
        return new MinIoBucketAccess(client, bucket, prefix);
    }
}
