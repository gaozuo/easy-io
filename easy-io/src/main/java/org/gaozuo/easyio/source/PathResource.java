package org.gaozuo.easyio.source;

import org.gaozuo.easyio.access.PathAccess;

public interface PathResource {

    String path();

    PathAccess access();
}
