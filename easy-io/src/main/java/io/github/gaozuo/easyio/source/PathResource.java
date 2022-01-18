package io.github.gaozuo.easyio.source;

import io.github.gaozuo.easyio.access.PathAccess;

public interface PathResource {

    String path();

    PathAccess access();
}
