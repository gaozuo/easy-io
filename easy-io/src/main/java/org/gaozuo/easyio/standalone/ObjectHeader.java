package org.gaozuo.easyio.standalone;

public class ObjectHeader {

    private static final String HEADER_PREFIX = "ei-";

    public static final ObjectHeader CONTENT_TYPE = new ObjectHeader("content-type", false, String.class);
    public static final ObjectHeader LENGTH = new ObjectHeader("length", false, Number.class);
    public static final ObjectHeader NAME = new ObjectHeader("object-name", false, String.class);

    private final String key;
    private final boolean multiValue;
    private final Class<?> valueType;

    public ObjectHeader(String key, boolean multiValue, Class<?> valueType) {
        if (key == null) {
            throw new IllegalArgumentException("Header key must be not null!");
        }
        if (!key.startsWith(HEADER_PREFIX)) {
            key = HEADER_PREFIX + key;
        }
        this.key = key.toLowerCase();
        this.multiValue = multiValue;
        this.valueType = valueType;
    }

    public String convert(Object value) {
        return value != null && valueType.isAssignableFrom(value.getClass()) ? value.toString() : null;
    }

    public String getKey() {
        return key;
    }

    public boolean isMultiValue() {
        return multiValue;
    }

    public Class<?> getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return key;
    }
}
