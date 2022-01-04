package org.gaozuo.easyminio.operation;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import org.assertj.core.api.Assertions;
import org.gaozuo.easyio.access.ObjectAccess;
import org.gaozuo.easyio.access.PathAccess;
import org.gaozuo.easyio.access.ResourceBucket;
import org.gaozuo.easyio.source.ObjectResource;
import org.gaozuo.easyio.source.PathResource;
import org.gaozuo.easyminio.MinIoCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinIoObjectTest {

    private MinioClient client;

    @Before
    public void setUp() {
        client = MinIoCreator.client();
        try {
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
    public void testNotExists() throws Exception {
        Assertions.assertThat(MinIoBucket.create(client).path("hr-objects").deep("lol/dog.jpg").buildObject().accessObject().exists()).isFalse();
    }

    @Test
    public void testGetAndExists() throws Exception {
        MinIoLocation location = MinIoLocation.fromBucket("hr-objects", client);
        ObjectResource objectResource = getOne(location.access().bucket().listPathResource());
        Assertions.assertThat(objectResource).isNull();
        ObjectAccess oa = MinIoBucket.create(client).path("hr-objects").deep("kio").buildObject().accessObject();
        oa.put(new ByteArrayInputStream("112".getBytes()), "112".getBytes().length, null);
        Assertions.assertThat(oa.exists()).isTrue();
        Assertions.assertThat(oa.getObject().getSource().read()).isNotEqualTo(-1);
    }

    @Test
    public void testPut() throws Exception {
        MinIoLocation location = MinIoLocation.fromBucket("hr-objects", client);
        ObjectResource objectResource = getOne(location.access().bucket().listPathResource());
        Assertions.assertThat(objectResource).isNull();
        ObjectAccess oa = MinIoBucket.create(client).path("hr-objects/jjk").buildObject().accessObject();
        Assertions.assertThat(oa.exists()).isFalse();
        byte[] a = "{name: 'abc'}".getBytes();
        byte[] b = "{name: 'def'}".getBytes();
        byte[] buf = new byte[1024];

        oa.put(new ByteArrayInputStream(a), a.length, null);
        oa.getObject().getSource().read(buf);
        Assertions.assertThat(buf).contains(a);
        oa.put(new ByteArrayInputStream(b), b.length, null);
        oa.getObject().getSource().read(buf);
        Assertions.assertThat(buf).contains(b);
    }

    private ObjectResource getOne(List<PathResource> resourceList) throws Exception {
        for (PathResource resource : resourceList) {
            System.out.println("path resource: " + resource.path());
            PathAccess access = resource.access();
            if (access.canUse()) {
                ResourceBucket rb = access.bucket();
                if (rb.hasObjectResource()) {
                    return rb.listObjectResource().get(0);
                }
                if (rb.hasPathResource()) {
                    return getOne(rb.listPathResource());
                }
            }
        }
        return null;
    }
}
