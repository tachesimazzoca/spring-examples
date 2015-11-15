package com.github.tachesimazzoca.spring.examples.forum.util;

import java.util.HashMap;
import java.util.Map;

public class ParameterUtils {
    public static Map<String, Object> params(Object... args) {
        Map<String, Object> m = new HashMap<String, Object>();
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "The number of args must be even.");
        for (int i = 0; i < args.length; i += 2) {
            if (args[i + 1] != null)
                m.put((String) args[i], args[i + 1]);
        }
        return m;
    }

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
