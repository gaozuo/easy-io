package org.gaozuo.easyio.access;

import org.gaozuo.easyio.source.ObjectResource;
import org.gaozuo.easyio.source.PathResource;

import java.util.List;

public interface ResourceBucket {

    boolean hasPathResource();

    boolean hasObjectResource();

    boolean hasError();

    List<PathResource> listPathResource();

    List<ObjectResource> listObjectResource();

    List<Throwable> listError();
}
