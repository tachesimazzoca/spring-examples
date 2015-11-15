package com.github.tachesimazzoca.spring.examples.forum.storage;

import java.util.Optional;

public interface Storage<T> {
    public String create(T value);

    public Optional<T> read(String key);

    public void write(String key, T value);

    public void delete(String key);
}
