package io.github.gaozuo.easyminio.operation;

import io.minio.*;
import org.assertj.core.api.Assertions;
import io.github.gaozuo.easyio.source.PathResource;
import io.github.gaozuo.easyio.source.PathResourceBuilder;
import io.github.gaozuo.easyminio.MinIoCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MinIoBucketTest {

    private MinioClient client;

    @Before
    public void setUp() {
        client = MinIoCreator.client();
        try {
            client.listObjects(ListObjectsArgs.builder().bucket("hr-objects").build()).forEach((item) -> {
                try {
                    client.removeObject(RemoveObjectArgs.builder().bucket("hr-objects").object(item.get().objectName()).build());
                } catch (Exception e) {
                }
            });

            client.makeBucket(MakeBucketArgs.builder().bucket("hr-objects").build());
        } catch (Exception e) {
        }
    }

    @After
    public void teardown() {
        try {
            client.removeBucket(RemoveBucketArgs.builder().bucket("hr-objects").build());
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotAllowedBucket() {
        Assertions.assertThatThrownBy(() -> {
            MinIoBucket.create(client).path("uui").bucket("hrs/org").buildObject();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testBuildPathResourceDetail() {
        String[] urls = new String[]{"hrs/bbs", "bbs"};
        for (String url : urls) {
            PathResourceBuilder builder = MinIoBucket.create(client).path(url);
            MinIoLocation location = (MinIoLocation)builder.buildPath();
            Assertions.assertThat(location.path()).isEqualTo(url);
            System.out.println(location.path());
        }
    }

    @Test
    public void testBuildPathDeep() {
        PathResource pathResource = MinIoBucket.create(client).bucket("hrs").path("org/223").deep("deep1").deep("deep2").buildPath();
        System.out.println(pathResource.path());
    }

    @Test
    public void testBuildObjectResourceDetail() {
        String[] urls = new String[]{"hrs/o/abc.jpg", "hrs/org/o/resume.txt", "hrs/o/info"};
        for (String url : urls) {
            PathResourceBuilder builder = MinIoBucket.create(client).path(url);
            MinIoObject object = (MinIoObject)builder.buildObject();
            Assertions.assertThat(object.path())
                .isEqualTo(url).contains("hrs/");
            System.out.println("path: " + object.path());
            Assertions.assertThat(object.parentPath()).endsWith("o");
            System.out.println("parent path: " + object.parentPath());
            Assertions.assertThat(object.name()).doesNotContain("hr");
            System.out.println("name: " + object.name());
        }
    }

    @Test
    public void reBuildMinIoBucket() {
        String[] paths = new String[]{"hr", "org", "you"};
        String[] buckets = new String[]{"image", "music", "video"};
        MinIoBucket builder = MinIoBucket.create(client).path("aa");
        for (String path : paths) {
            for (String bucket : buckets) {
                builder = builder.path(path).bucket(bucket);
            }
        }
        PathResource path = builder.buildPath();
        Assertions.assertThat(path.path()).isEqualTo("video/you");
    }

}
