package io.github.gaozuo.easyio.access;

public interface PathAccess {

    boolean canUse();

    void construct() throws Exception;

    ResourceBucket bucket();
}
