package org.gaozuo.easyio.source;

public interface PathResourceBuilder {

    PathResourceBuilder path(String path);

    PathResourceBuilder deep(String path);

    PathResource buildPath();

    ObjectResource buildObject();
}
