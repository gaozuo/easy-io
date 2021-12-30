package org.gaozuo.easyio.source;

import org.gaozuo.easyio.access.ObjectAccess;

public interface ObjectResource {

    String path();

    String parentPath();

    String name();

    ObjectAccess accessObject();
}
