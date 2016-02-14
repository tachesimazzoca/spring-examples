package com.github.tachesimazzoca.spring.examples.forum.helpers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TempFileHelper {
    private File directory;

    public TempFileHelper(File directory) {
        if (!directory.exists() || !directory.isDirectory())
            throw new IllegalArgumentException("The directory does not exist.");
        this.directory = directory;
    }

    public TempFileHelper(String directoryPath) {
        this(new File(directoryPath));
    }

    public File create(InputStream input, String prefix, String suffix)
            throws IOException {
        File tempFile = File.createTempFile(prefix, suffix, directory);
        FileUtils.copyInputStreamToFile(input, tempFile);
        return tempFile;
    }

    public Optional<File> read(String name) {
        // To avoid directory traversal, convert the given file name into the basename.
        String nm = FilenameUtils.getName(name);

        File f = new File(directory, nm);
        if (!f.exists() || !f.isFile())
            return Optional.empty();
        else
            return Optional.of(f);
    }
}
