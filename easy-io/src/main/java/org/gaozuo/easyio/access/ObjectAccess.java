package org.gaozuo.easyio.access;

import org.gaozuo.easyio.standalone.ObjectSource;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public interface ObjectAccess {

    /**
     * Is object exists
     * @return
     * @throws Exception
     */
    boolean exists() throws Exception;

    /**
     * Get Object source
     * @return
     * @throws Exception
     */
    ObjectSource getObject() throws Exception;

    /**
     * Remove current object
     * @throws Exception
     */
    void remove() throws Exception;

    /**
     * Put Object source
     * @param source
     * @param size
     * @param headers
     * @throws Exception
     */
    void put(InputStream source, long size, Map<String, ? extends Collection<String>> headers) throws Exception;
}
