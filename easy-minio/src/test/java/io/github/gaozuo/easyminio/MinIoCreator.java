package io.github.gaozuo.easyminio;

import io.minio.MinioClient;

public abstract class MinIoCreator {

    public static MinioClient client() {
        return MinioClient.builder()
            .credentials("minio", "minio123")
            .endpoint("http://127.0.0.1:9000/")
              .build();
    }

}
