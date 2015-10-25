package com.github.tachesimazzoca.spring.examples.forum.config;

public interface Config {
    Object get(String key);

    <T> T get(String key, Class<T> type);
}
