package org.gaozuo.easyminio.operation;

import io.minio.MinioClient;
import org.gaozuo.easyio.access.ObjectAccess;
import org.gaozuo.easyio.source.ObjectResource;
import org.gaozuo.easyminio.MinIoUtils;
import org.gaozuo.easyminio.access.MinIoObjectAccess;

import java.util.List;

public class MinIoObject implements ObjectResource {

    private final String bucket;
    private final String object;
    private final MinioClient client;
    private final String parentPath;

    private MinIoObject(String bucket, String parentPath, String object, MinioClient client) {
        if (!MinIoUtils.checkBucket(bucket)) {
            throw new IllegalArgumentException("Not a valid bucket: " + bucket);
        }
        if (!MinIoUtils.checkObject(object)) {
            throw new IllegalArgumentException("Not a valid object: " + object);
        }
        this.bucket = bucket;
        this.object = object;
        this.client = client;
        this.parentPath = parentPath;
    }

    public static MinIoObject create(String bucket, String object, MinioClient client) {
        List<String> paths = MinIoUtils.toPathToken(object);
        String prefix = null;
        if (paths.size() > 1) {
            paths.remove(paths.size() - 1);
            prefix = MinIoUtils.concatToken(paths);
        }
        return new MinIoObject(bucket, MinIoUtils.checkPath(prefix)
                ? MinIoUtils.concatToken(bucket, prefix) : bucket, object, client);
    }

    @Override
    public String path() {
        return MinIoUtils.concatToken(bucket, name());
    }

    @Override
    public String parentPath() {
        return parentPath;
    }

    @Override
    public String name() {
        return object;
    }

    @Override
    public ObjectAccess accessObject() {
        return new MinIoObjectAccess(bucket, object, client);
    }
}
