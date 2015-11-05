package com.github.tachesimazzoca.spring.examples.forum.models;

import com.google.common.base.Optional;

public interface Storage<T> {
    public String create(T value);

    public Optional<T> read(String key);

    public void write(String key, T value);

    public void delete(String key);
}
