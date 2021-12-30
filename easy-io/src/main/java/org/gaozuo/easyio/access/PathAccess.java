package org.gaozuo.easyio.access;

public interface PathAccess {

    boolean canUse() throws Exception;

    void construct() throws Exception;

    ResourceBucket bucket() throws Exception;
}
