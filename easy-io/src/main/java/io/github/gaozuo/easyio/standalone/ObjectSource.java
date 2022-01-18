package io.github.gaozuo.easyio.standalone;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectSource {

    private final Map<String, ? extends Collection<String>> headers;
    private final InputStream source;

    public ObjectSource(Map<String, ? extends Collection<String>> headers, InputStream source) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;
        this.source = source;
    }

    public Map<String, ? extends Collection<String>> getHeaders() {
        return headers;
    }

    public InputStream getSource() {
        return source;
    }
}
