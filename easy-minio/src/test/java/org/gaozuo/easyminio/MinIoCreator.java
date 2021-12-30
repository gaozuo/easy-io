package org.gaozuo.easyminio;

import io.minio.MinioClient;

public abstract class MinIoCreator {

    public static MinioClient client() {
        return MinioClient.builder()
            .credentials("minio", "minio123")
            .endpoint("http://10.88.79.154:9000/")
              .build();
    }

}
