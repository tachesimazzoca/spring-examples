package com.github.tachesimazzoca.spring.examples.forum.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ProfileIconHelper {
    private FileHelper fileHelper;

    public ProfileIconHelper(String directoryPath) {
        fileHelper = new FileHelper(new File(directoryPath),
                FileHelper.TYPES_IMAGE, FileHelper.NAMING_NUMERIC_TREE);
    }

    public Optional<FileHelper.Result> find(String name) {
        return fileHelper.find(name);
    }

    public void save(InputStream input, String name, String extension) throws IOException {
        fileHelper.save(input, name, extension);
    }

    public void delete(String name) {
        fileHelper.delete(name);
    }
}
