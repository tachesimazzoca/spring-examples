package com.github.tachesimazzoca.spring.examples.forum.util;

public class SystemTimer implements Timer {
    @Override
    public Long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
