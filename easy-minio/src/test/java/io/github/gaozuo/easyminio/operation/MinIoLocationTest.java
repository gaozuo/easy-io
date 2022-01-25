package io.github.gaozuo.easyminio.operation;

import io.github.gaozuo.easyminio.MinIoCreator;
import io.minio.*;
import org.assertj.core.api.Assertions;
import io.github.gaozuo.easyio.access.PathAccess;
import io.github.gaozuo.easyio.access.ResourceBucket;
import io.github.gaozuo.easyio.source.ObjectResource;
import io.github.gaozuo.easyio.source.PathResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MinIoLocationTest {

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
    public void testMinIoLocationNotAllowed() {
        Assertions.assertThatThrownBy(() -> {
            MinIoLocation.fromBucketAndPrefix("hrs/tt", "org", client);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testMinIoLocationDetail() {
        MinIoLocation location = MinIoLocation.fromBucketAndPrefix("hrs", "org/tt", client);
        Assertions.assertThat(location.path()).isEqualTo("hrs/org/tt");
    }

    @Test
    public void testMinIoLocationListResource() throws Exception {
        MinIoLocation location = MinIoLocation.fromBucketAndPrefix("hrs", "org/tt", client);
        Assertions.assertThat(location.access().canUse()).isFalse();
        location = MinIoLocation.fromBucketAndPrefix("hr-objects", "org/tt", client);
        Assertions.assertThat(location.access().canUse()).isTrue();
        location.access().bucket();

        location = MinIoLocation.fromBucket("hr-objects", client);
        ResourceBucket resourceBucket = location.access().bucket();
        Assertions.assertThat(resourceBucket.hasPathResource()).isFalse();
        listResource(resourceBucket.listPathResource());
    }

    private void listResource(List<PathResource> resourceList) throws Exception {
        for (PathResource resource : resourceList) {
            System.out.println("path resource: " + resource.path());
            PathAccess access = resource.access();
            if (access.canUse()) {
                ResourceBucket rb = access.bucket();
                if (rb.hasObjectResource()) {
                    for (ObjectResource obj : rb.listObjectResource()) {
                        System.out.println("object resource: " + obj.path());
                    }
                }
                if (rb.hasPathResource()) {
                    listResource(rb.listPathResource());
                }
            }
        }
    }



}
