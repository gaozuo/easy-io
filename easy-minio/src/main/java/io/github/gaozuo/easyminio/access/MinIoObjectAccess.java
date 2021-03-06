package io.github.gaozuo.easyminio.access;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.gaozuo.easyminio.MinIoUtils;
import io.minio.*;
import io.minio.errors.*;
import io.github.gaozuo.easyio.access.ObjectAccess;
import io.github.gaozuo.easyio.standalone.ObjectSource;

import java.io.InputStream;
import java.util.*;

public class MinIoObjectAccess implements ObjectAccess {

    private static final String AMZ_META_PREFIX = "x-amz-meta-";
    private static final int AMZ_META_PREFIX_OFFSET = AMZ_META_PREFIX.length();

    private final String bucket;
    private final String object;
    private final MinioClient client;

    public MinIoObjectAccess(String bucket, String object, MinioClient client) {
        if (!MinIoUtils.checkBucket(bucket)) {
            throw new IllegalArgumentException("Not a valid bucket: " + bucket);
        }
        this.bucket = bucket;
        this.object = object;
        this.client = client;
    }

    @Override
    public boolean exists() {
        boolean exists = true;
        try (GetObjectResponse ignored = client.getObject(
            GetObjectArgs
              .builder()
              .object(object)
              .bucket(bucket)
                .build()
        )) {
        } catch (ErrorResponseException e) {
            if (e.errorResponse() != null
                    && "NoSuchKey".equals(e.errorResponse().code())) {
                exists = false;
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return exists;
    }

    @Override
    public Map<String, ? extends Collection<String>> getHeaders() {
        try {
            StatObjectResponse ops = client.statObject(StatObjectArgs.builder().bucket(bucket).object(object).build());
            Map<String, List<String>> headers = new HashMap<>();
            ops.userMetadata().forEach((k, v) -> {
                headers.put(k, Collections.singletonList(v));
            });
            return headers;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ObjectSource getObject() {
        try {
            GetObjectResponse ops = client.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                      .build()
            );
            Map<String, List<String>> headers = new HashMap<>();
            ops.headers().toMultimap().forEach((key, val) -> {
                if (key != null) {
                    int find = key.indexOf(AMZ_META_PREFIX);
                    if (find > -1) {
                        headers.put(key.substring(find + AMZ_META_PREFIX_OFFSET), val);
                    }
                }
            });
            return new ObjectSource(headers, ops);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public void remove() throws Exception {
        client.removeObject(
            RemoveObjectArgs
              .builder()
                .object(object)
                .bucket(bucket)
                  .build()
        );
    }

    @Override
    public void put(InputStream source, long size, Map<String, ? extends Collection<String>> headers) throws Exception {
        PutObjectArgs.Builder builder = PutObjectArgs
            .builder()
              .bucket(bucket)
              .object(object)
              .stream(source, size, 0);

        if (headers != null) {
            Multimap<String, String> multimap = ArrayListMultimap.create();
            for (String name : headers.keySet()) {
                multimap.putAll(name, headers.get(name));
            }
            builder.userMetadata(multimap);
        }

        client.putObject(builder.build());
    }
}
