package io.github.gaozuo.easyio.access;

import io.github.gaozuo.easyio.standalone.ObjectSource;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public interface ObjectAccess {

    /**
     * Is object exists
     */
    boolean exists();

    /**
     * Get Object headers
     */
    Map<String, ? extends Collection<String>> getHeaders();

    /**
     * Get Object source
     */
    ObjectSource getObject();

    /**
     * Remove current object
     */
    void remove() throws Exception;

    /**
     * Put Object source
     * @param source object data
     * @param size object size
     * @param headers can be null
     */
    void put(InputStream source, long size, Map<String, ? extends Collection<String>> headers) throws Exception;
}
