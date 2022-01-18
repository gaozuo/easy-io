package io.github.gaozuo.easyio.standalone;

import java.util.Collection;
import java.util.Map;

public abstract class ObjectInfoManager {

    public static String getStandValueByKey(Map<String, ? extends Collection<String>> headers, ObjectHeader header) {
        Collection<String> values = headers.get(header.getKey());
        return header.getStandHeader(values);
    }

    public static String getStandValue(Map<ObjectHeader, ? extends Collection<String>> headers, ObjectHeader header) {
        Collection<String> values = headers.get(header);
        return header.getStandHeader(values);
    }
}
