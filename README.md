# easy-io
easy operation for resources.

You can easily manipulate resources without paying attention to the low-level details.

Like minio service you can use easy io api
--------------------------------------------------------------

    private PathResourceBuilder createResourceBuilder(String bucket, MinioClient client) {
        try {
            PathResourceBuilder upload = MinIoBucket.create(client).bucket(bucket);
            PathAccess access = upload.buildPath().access();
            if (!access.canUse()) {
                access.construct();
            }
            return upload;
        } catch (Exception e) {
            throw new UnsupportedOperationException("cannot create upload resources builder!", e);
        }
    }

    PathResourceBuilder bd = createResourceBuilder("/bucket", client).deep("/music");
    Assertions.assertTrue(bd.buildPath().access().canUse())
--------------------------------------------------------------

Maven for easy-minio:
---------------------------------------------------------------
    <dependency>
        <groupId>io.github.gaozuo</groupId>
        <artifactId>easy-minio</artifactId>
        <version>0.1</version>
        <scope>compile</scope>
    </dependency>
---------------------------------------------------------------










