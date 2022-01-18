package io.github.gaozuo.easyio.source;

import io.github.gaozuo.easyio.access.ObjectAccess;

public interface ObjectResource {

    String path();

    String parentPath();

    String name();

    ObjectAccess accessObject();
}
