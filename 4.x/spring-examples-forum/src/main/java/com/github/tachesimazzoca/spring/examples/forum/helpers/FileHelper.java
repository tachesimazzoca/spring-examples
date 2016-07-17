package com.github.tachesimazzoca.spring.examples.forum.helpers;

import com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileHelper {
    private final File directory;
    private final Map<String, String> mimeTypes;
    private final NamingStrategy namingStrategy;

    public static final Map<String, String> TYPES_IMAGE =
            ParameterUtils.<String, String>map(
                    "jpg", "image/jpeg",
                    "gif", "image/gif",
                    "png", "image/png"
            );

    public static final FileHelper.NamingStrategy NAMING_NUMERIC_TREE =
            new NamingStrategy() {
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

    public FileHelper(File directory) {
        this.directory = directory;
        this.mimeTypes = createDefaultMimeTypes();
        this.namingStrategy = createDefaultNamingStrategy();
    }

    public FileHelper(File directory, Map<String, String> mimeTypes) {
        this.directory = directory;
        this.mimeTypes = mimeTypes;
        this.namingStrategy = createDefaultNamingStrategy();
    }

    public FileHelper(File directory, Map<String, String> mimeTypes,
                      NamingStrategy namingStrategy) {
        this.directory = directory;
        this.mimeTypes = mimeTypes;
        this.namingStrategy = namingStrategy;
    }

    private static Map<String, String> createDefaultMimeTypes() {
        Map<String, String> m = new HashMap<>();
        m.put("", "application/octet-stream");
        return m;
    }

    private static NamingStrategy createDefaultNamingStrategy() {
        return new NamingStrategy() {
            public String buildRelativePath(String name, String extension) {
                String nm = FilenameUtils.getBaseName(name);
                if (extension.isEmpty())
                    return nm;
                else
                    return nm + "." + extension;
            }
        };
    }

    public Optional<Result> find(String name) {
        for (Map.Entry<String, String> entry : mimeTypes.entrySet()) {
            final File f = new File(directory,
                    namingStrategy.buildRelativePath(name, entry.getKey()));
            if (f.exists() && f.isFile())
                return Optional.of(new Result(f, entry.getValue()));
        }
        return Optional.empty();
    }

    public Optional<Result> find(String name, String extension) {
        if (!mimeTypes.containsKey(extension))
            return Optional.empty();
        final String mimeType = mimeTypes.get(extension);
        final File f = new File(directory,
                namingStrategy.buildRelativePath(name, extension));
        if (f.exists() && f.isFile())
            return Optional.of(new Result(f, mimeType));
        else
            return Optional.empty();
    }

    public boolean save(InputStream input, String name) throws IOException {
        return save(input, name, "");
    }

    public boolean save(InputStream input, String name, String extension) throws IOException {
        if (!mimeTypes.containsKey(extension))
            return false;
        final File f = new File(directory,
                namingStrategy.buildRelativePath(name, extension));
        if (f.exists() && !f.isFile())
            return false;
        FileUtils.copyInputStreamToFile(input, f);
        return true;
    }

    public boolean delete(String name) {
        boolean success = true;
        for (Map.Entry<String, String> entry : mimeTypes.entrySet()) {
            if (!delete(name, entry.getKey()))
                success = false;
        }
        return success;
    }

    public boolean delete(String name, String extension) {
        final File f = new File(directory,
                namingStrategy.buildRelativePath(name, extension));
        if (!f.exists())
            return true;
        if (!f.isFile())
            return false;
        return f.delete();
    }

    public interface NamingStrategy {
        public String buildRelativePath(String name, String extension);
    }

    public static class Result {
        private final File file;
        private final String mimeType;

        public Result(File file, String mimeType) {
            this.file = file;
            this.mimeType = mimeType;
        }

        public File getFile() {
            return file;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
}
