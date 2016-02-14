package com.github.tachesimazzoca.spring.examples.forum.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterUtils {
    public static Map<String, Object> params(Object... args) {
        return ParameterUtils.<String, Object>map(args);
    }

    @SafeVarargs
    public static <T> List<T> list(T... args) {
        return Arrays.asList(args);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> map(Object... args) {
        Map<K, V> m = new HashMap<K, V>();
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "The number of args must be even.");
        for (int i = 0; i < args.length; i += 2) {
            if (args[i + 1] != null)
                m.put((K) args[i], (V) args[i + 1]);
        }
        return m;
    }

    @SafeVarargs
    public static <T> T nullTo(T... args) {
        for (T arg : args) {
            if (arg != null)
                return arg;
        }
        return null;
    }

    public static String emptyTo(String... args) {
        for (String arg : args) {
            if (arg != null && !arg.isEmpty())
                return arg;
        }
        return null;
    }
}
