package com.github.tachesimazzoca.spring.examples.forum.storage;

import java.io.InputStream;
import java.util.Optional;

public interface StorageEngine {
    public Optional<InputStream> read(String key);

    public void write(String key, InputStream value);

    public void delete(String key);
}
