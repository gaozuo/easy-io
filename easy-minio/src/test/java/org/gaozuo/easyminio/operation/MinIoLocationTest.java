package org.gaozuo.easyminio.operation;

import io.minio.MinioClient;
import org.assertj.core.api.Assertions;
import org.gaozuo.easyio.access.PathAccess;
import org.gaozuo.easyio.access.ResourceBucket;
import org.gaozuo.easyio.source.ObjectResource;
import org.gaozuo.easyio.source.PathResource;
import org.gaozuo.easyminio.MinIoCreator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MinIoLocationTest {

    private MinioClient client;

    @Before
    public void setUp() {
        client = MinIoCreator.client();
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
        Assertions.assertThat(resourceBucket.hasPathResource()).isTrue();
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
