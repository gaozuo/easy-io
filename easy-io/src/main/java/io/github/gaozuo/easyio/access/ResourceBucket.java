package io.github.gaozuo.easyio.access;

import io.github.gaozuo.easyio.source.ObjectResource;
import io.github.gaozuo.easyio.source.PathResource;

import java.util.List;

public interface ResourceBucket {

    boolean hasPathResource();

    boolean hasObjectResource();

    boolean hasError();

    List<PathResource> listPathResource();

    List<ObjectResource> listObjectResource();

    List<Throwable> listError();
}
