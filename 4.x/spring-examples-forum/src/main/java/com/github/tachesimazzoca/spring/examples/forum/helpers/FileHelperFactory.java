package com.github.tachesimazzoca.spring.examples.forum.helpers;

import com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Map;

public class FileHelperFactory {
    public static final Map<String, String> TYPES_IMAGE =
            ParameterUtils.<String, String>map(
                    "jpg", "image/jpeg",
                    "gif", "image/gif",
                    "png", "image/png"
            );

    public static final FileHelper.NamingStrategy NAMING_NUMERIC_TREE =
            new FileHelper.NamingStrategy() {
                public String buildRelativePath(String name, String extension) {
                    if (!StringUtils.isNumeric(name))
                        throw new IllegalArgumentException("The name must contain only digits.");
                    StringBuilder sb = new StringBuilder();
                    int max = name.length() - 1;
                    for (int i = 0; i < max; i++) {
                        sb.append(name.substring(i, i + 1));
                        sb.append("/");
                    }
                    sb.append(name);
                    sb.append(".");
                    sb.append(extension);
                    return sb.toString();
                }
            };

    public static FileHelper createProfileIconHelper(String directoryPath) {
        return createProfileIconHelper(new File(directoryPath));
    }

    public static FileHelper createProfileIconHelper(File directory) {
        return new FileHelper(directory, TYPES_IMAGE, NAMING_NUMERIC_TREE);
    }
}
