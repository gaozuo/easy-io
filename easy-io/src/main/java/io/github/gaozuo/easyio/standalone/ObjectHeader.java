package io.github.gaozuo.easyio.standalone;

import java.util.Collection;

public class ObjectHeader<T> {

    private static final String HEADER_PREFIX = "ei-";

    public static final ObjectHeader CONTENT_TYPE = ObjectHeader.create("content-type", false, ",", String.class);
    public static final ObjectHeader LENGTH = ObjectHeader.create("length", false, ",", Number.class);
    public static final ObjectHeader NAME = ObjectHeader.create("object-name", false, ",", String.class);

    private final String key;
    private final boolean multiValue;
    private final String joinToken;
    private final Class<T> valueType;

    private ObjectHeader(String key, boolean multiValue, String joinToken, Class<T> valueType) {
        if (key == null) {
            throw new IllegalArgumentException("Header key must be not null!");
        }
        if (!key.startsWith(HEADER_PREFIX)) {
            key = HEADER_PREFIX + key;
        }
        this.joinToken = joinToken;
        this.key = key.toLowerCase();
        this.multiValue = multiValue;
        this.valueType = valueType;
    }

    public static <T> ObjectHeader<T> create(String key, boolean multiValue, String joinToken, Class<T> valueType) {
        return new ObjectHeader<>(key, multiValue, joinToken, valueType);
    }

    public String getStandHeader(Collection<String> values) {
        return values != null ? (
            multiValue ? String.join(joinToken, values) : values.iterator().next()
        ) : null;
    }

    public T convert(Object value) {
        return value != null && valueType.isAssignableFrom(value.getClass()) ? valueType.cast(value) : null;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
