package org.gaozuo.easyminio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MinIoUtils {

    private static final String MIN_IO_TOKEN_SEPARATOR = "/";

    public static String concatToken(String... tokens) {
        return concatToken(Arrays.asList(tokens));
    }

    public static String concatToken(Iterable<String> tokens) {
        List<String> paths = new ArrayList<>();
        for (String object : tokens) {
            object = formatPath(object);
            if (object != null && !object.isEmpty()) {
                paths.add(object);
            }
        }
        return String.join(MIN_IO_TOKEN_SEPARATOR, paths);
    }

    public static boolean checkPath(String prefix) {
        return prefix != null && !prefix.isEmpty();
    }

    public static boolean checkBucket(String bucket) {
        return bucket != null
            && !bucket.isEmpty()
            && !bucket.contains(MIN_IO_TOKEN_SEPARATOR)
            && (bucket.length() >= 3 && bucket.length() <= 63);
    }

    public static boolean checkObject(String object) {
        return object != null && !object.isEmpty();
    }

    private static String formatPath(String path) {
        if (path != null) {
            if (path.startsWith(MinIoUtils.MIN_IO_TOKEN_SEPARATOR)) {
                path = path.substring(1);
            }
            if (path.endsWith(MinIoUtils.MIN_IO_TOKEN_SEPARATOR)) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    public static List<String> toPathToken(String path) {
        List<String> paths = new ArrayList<>();
        if (path != null) {
            paths = new ArrayList<>(Arrays.asList(path.split(MIN_IO_TOKEN_SEPARATOR)));
        }
        return paths;
    }
}
