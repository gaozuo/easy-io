package org.gaozuo.easyminio.operation;

import io.minio.MinioClient;
import org.gaozuo.easyio.source.ObjectResource;
import org.gaozuo.easyio.source.PathResource;
import org.gaozuo.easyio.source.PathResourceBuilder;
import org.gaozuo.easyminio.MinIoUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MinIoBucket implements PathResourceBuilder {

    private final MinioClient client;
    private final List<String> path;
    private final String bucket;

    private final boolean usePath;
    private final boolean useBucket;

    private MinIoBucket(String bucket, MinioClient client,
            List<String> path, boolean usePath, boolean useBucket) {
        if (path == null) {
            path = new ArrayList<>();
        }
        this.client = client;
        this.bucket = bucket;
        this.path = path;
        this.usePath = usePath;
        this.useBucket = useBucket;
    }

    public static MinIoBucket create(MinioClient client) {
        return new MinIoBucket(null, client, null, false, false);
    }

    public MinIoBucket bucket(String bucket) {
        return create(this.path, bucket, this.client, this.usePath, true);
    }

    @Override
    public MinIoBucket path(String path) {
        return create(Collections.singletonList(path), this.bucket, this.client, true, this.useBucket);
    }

    @Override
    public PathResourceBuilder deep(String path) {
        List<String> paths = new ArrayList<>(this.path);
        paths.add(path);
        return create(paths, this.bucket, this.client, true, this.useBucket);
    }

    private MinIoBucket create(List<String> path, String bucket, MinioClient client, boolean usePath, boolean useBucket) {
        return new MinIoBucket(bucket, client, path, usePath, useBucket);
    }

    @Override
    public PathResource buildPath() {
        List<String> bucketAndPath = getBucketAndPath();
        if (bucketAndPath.size() > 1) {
            return MinIoLocation.fromBucketAndPrefix(bucketAndPath.get(0), bucketAndPath.get(1), client);
        } else {
            return MinIoLocation.fromBucket(bucketAndPath.get(0), client);
        }
    }

    @Override
    public ObjectResource buildObject() {
        List<String> bucketAndPath = getBucketAndPath();
        if (bucketAndPath.size() <= 1) {
            throw new UnsupportedOperationException("Cannot build a object resource.");
        }
        return MinIoObject.create(bucketAndPath.get(0), bucketAndPath.get(1), client);
    }

    protected List<String> getBucketAndPath() {
        List<String> bucketAndPath = new ArrayList<>();
        if (useBucket) {
            if (!MinIoUtils.checkBucket(bucket)) {
                throw new IllegalArgumentException("Not a valid bucket: " + bucket);
            }
            bucketAndPath.add(bucket);
        }
        if (usePath) {
            if (path.isEmpty()) {
                throw new IllegalStateException("Path must be not null!");
            }
            String full = MinIoUtils.concatToken(
                path.stream().map((p) -> MinIoUtils.concatToken(MinIoUtils.toPathToken(p)))
                  .collect(Collectors.toList())
            );
            if (!MinIoUtils.checkPath(full)) {
                throw new IllegalArgumentException("Not a valid path: " + full);
            }
            List<String> tokenList = MinIoUtils.toPathToken(full);
            if (bucketAndPath.isEmpty() && !tokenList.isEmpty()) {
                bucketAndPath.add(tokenList.remove(0));
            }
            if (!tokenList.isEmpty()) {
                bucketAndPath.add(MinIoUtils.concatToken(tokenList));
            }
        }
        return bucketAndPath;
    }
}
